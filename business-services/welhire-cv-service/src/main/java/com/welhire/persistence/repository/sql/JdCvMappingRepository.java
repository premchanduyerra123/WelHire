package com.welhire.persistence.repository.sql;

import com.welhire.persistence.entity.sql.JdCvMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JdCvMappingRepository extends JpaRepository<JdCvMapping, String> {

}
