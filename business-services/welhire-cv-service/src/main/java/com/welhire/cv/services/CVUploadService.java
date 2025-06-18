package com.welhire.cv.services;

import com.welhire.persistence.entity.sql.CvUpload;
import com.welhire.persistence.entity.sql.JdCvMapping;
import com.welhire.persistence.repository.sql.CvUploadRepository;
import com.welhire.persistence.repository.sql.JdCvMappingRepository;
import com.welhire.shared.dto.enums.ParseStatus;
import com.welhire.shared.dto.utils.FileHashUtil;
import com.welhire.shared.dto.v1.CVUploadResponse;
import com.welhire.shared.dto.v1.MultiCVUploadRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CVUploadService {

    private final CvUploadRepository uploadRepo;
    private final JdCvMappingRepository mappingRepo;
    private final FileSystemStorageService storage;
    private final CVParsingService parsing;
    private final MongoTemplate mongo;
    private final HashDuplicateChecker dupChecker;

    public List<CVUploadResponse> uploadFiles(MultiCVUploadRequest meta,
                                              List<MultipartFile> files) {

        List<CVUploadResponse> response = new ArrayList<>();
        String ts = String.valueOf(System.currentTimeMillis());

        for (MultipartFile file : files) {
            response.add(handleSingleFile(meta, file, ts));
        }
        return response;
    }

    private CVUploadResponse handleSingleFile(MultiCVUploadRequest meta,
                                              MultipartFile file,
                                              String ts) {

        String fileName = file.getOriginalFilename();

        try {
            /* 1) hash duplicate check */
            String hash = FileHashUtil.calculateMD5(file);
            CvUpload upload   = dupChecker.findDuplicate(hash).orElse(null);
            boolean  isFileDup = upload != null;

            /* 2) stop early if JD–CV link already exists */
            if (isFileDup && mappingRepo.existsByJdRefIdAndCvUploadRefId(meta.getJdRefId(), upload.getId())) {

                return new CVUploadResponse(
                        upload.getId(),
                        fileName,
                        "",
                        ParseStatus.CV_UPLOADED,
                        "Duplicate CV already attached to this JD");
            }

            /* 3) persist upload row if this is a brand-new file */
            if (!isFileDup) {
                String path = storage.storeFile(file, meta.getJdRefId(), meta.getEmail(), ts);
                upload = uploadRepo.save(CvUpload.builder()
                        .cvName(fileName)
                        .filePath(path)
                        .fileHash(hash)
                        .userEmail(meta.getEmail())
                        .parseStatus(ParseStatus.CV_UPLOADED)
                        .createdBy(meta.getEmail())
                        .build());
            }

            /* 4) create link row (guaranteed unique) */
            JdCvMapping link = mappingRepo.save(JdCvMapping.builder()
                    .jdRefId(meta.getJdRefId())
                    .cvUploadRefId(upload.getId())
                    .createdBy(meta.getEmail())
                    .build());

            /* 5) fire async parse/creation pipeline */
            parsing.parseAndCreateAsync(upload, meta, isFileDup);

            return new CVUploadResponse(
                    upload.getId(),
                    fileName,
                    upload.getFilePath(),
                    isFileDup ? ParseStatus.CV_PARSED_SUCCESS : ParseStatus.CV_UPLOADED,
                    isFileDup ? "Duplicate file: candidate pipeline started for this JD"
                            : "Uploaded and queued");

        } catch (IOException io) {
            return new CVUploadResponse(
                    null,
                    fileName,
                    null,
                    ParseStatus.CV_PARSED_FAILURE,
                    "Upload error: " + io.getMessage());
        }
    }


    public Page<CvUpload> listUploadsForJd(String jdId, Pageable pageable) {
        return uploadRepo.findAllByJd(jdId, pageable);
    }

    public Optional<CvUpload> getById(UUID id) {
        return uploadRepo.findById(id);
    }

    public Page<CvUpload> getAllUploads(Pageable pg) {
        return uploadRepo.findAll(pg);
    }

    public Page<CvUpload> searchBy(Map<String, String> filters, Pageable pg) {
        Query q = new Query();
        filters.forEach((k, v) ->
                q.addCriteria(Criteria.where(k).regex(".*" + v.trim() + ".*", "i")));
        long total = mongo.count(q, CvUpload.class);
        List<CvUpload> hits = mongo.find(q.with(pg), CvUpload.class);
        return new PageImpl<>(hits, pg, total);
    }
}
