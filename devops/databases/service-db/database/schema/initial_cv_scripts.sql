create table candidate_cv_upload
(
id varchar(255) not null primary key,
candidate_id varchar(255),
cv_name varchar(255),
file_hash varchar(255),
file_path varchar(255),
jd_ref_id varchar(255),
parse_end_time timestamp(6),
parse_error_message varchar(255),
parse_start_time timestamp(6),
parse_status varchar(255) ,
user_email varchar(255),
created_at timestamp(6), 
created_by varchar(255), 
updated_at timestamp(6), 
updated_by varchar(255)

)

create table jd_cv_mapping (
	id varchar(255)  primary key,
	cv_upload_ref_id varchar(255),
	jd_ref_id varchar(255),
	created_at timestamp(6), 
	created_by varchar(255), 
	updated_at timestamp(6), 
	updated_by varchar(255),
	UNIQUE (cv_upload_ref_id, jd_ref_id)
	)


