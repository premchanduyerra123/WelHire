
 create table cv_upload 
(
id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
file_hash  varchar(32)    NOT NULL UNIQUE,
cv_name          varchar(255),
file_path        varchar(255) NOT NULL,
user_email       varchar(255),

parse_status     varchar(255),
parse_start_time timestamp(6),
parse_end_time   timestamp(6),
parse_error_message  varchar(255),

candidate_create_status     varchar(255),
candidate_create_start_time timestamp(6),
candidate_create_end_time   timestamp(6),
candidate_create_error_message  varchar(255),

candidate_id varchar(255),

-- audit
created_at timestamp(6), 
created_by varchar(255), 
updated_at timestamp(6), 
updated_by varchar(255)

)
-- indexes that help typical queries
--CREATE INDEX idx_cv_upload_status    ON cv_upload (parse_status);
--CREATE INDEX idx_cv_upload_candidate ON cv_upload (candidate_id);


create table jd_cv_mapping (
	id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
	jd_ref_id varchar(255),
	cv_upload_ref_id uuid,
	created_at timestamp(6), 
	created_by varchar(255), 
	updated_at timestamp(6), 
	updated_by varchar(255),

    -- one CV can be mapped to the same JD only once
    CONSTRAINT uk_jd_cv UNIQUE (jd_ref_id, cv_upload_ref_id)
)


