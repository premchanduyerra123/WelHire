CREATE TABLE candidate_cv_uploads (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_email VARCHAR(255) NOT NULL,
    jd_id VARCHAR(100),
    cv_name VARCHAR(255) NOT NULL,
    azure_blob_path TEXT NOT NULL,
    is_parsed BOOLEAN DEFAULT FALSE,
    parse_start_time TIMESTAMP,
    parse_end_time TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);




CREATE TABLE parsed_candidate_data (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    jd_id VARCHAR(100),
    upload_id UUID NOT NULL REFERENCES candidate_cv_uploads(id) ON DELETE CASCADE,

    full_name VARCHAR(150), -- Typical full names rarely exceed 100 chars
    mobile_number VARCHAR(15), -- Avoid NUMERIC; use VARCHAR for leading +, multiple numbers
    email_id VARCHAR(150),

    date_of_birth VARCHAR(15), -- Store as DATE, AS VARCHAR
    age SMALLINT, -- Age: max 2 digits
    location VARCHAR(150),
    gender VARCHAR(10),
    marital_status VARCHAR(15),

    degree VARCHAR(100),
    university VARCHAR(150),
    graduation_year SMALLINT, -- Year as numeric type
    year_of_experience VARCHAR(5),

    strengths TEXT,
    summary_brief TEXT,

    email_reminder_count SMALLINT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    parsed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE parsed_projects (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    candidate_id UUID NOT NULL REFERENCES parsed_candidate_data(id) ON DELETE CASCADE,
    project_description TEXT
);


CREATE TABLE parsed_skills (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    candidate_id UUID NOT NULL REFERENCES parsed_candidate_data(id) ON DELETE CASCADE,
    skill_name VARCHAR(50)
);
CREATE TABLE parsed_languages (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    candidate_id UUID NOT NULL REFERENCES parsed_candidate_data(id) ON DELETE CASCADE,
    language VARCHAR(20)
);
CREATE TABLE parsed_strengths (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    candidate_id UUID NOT NULL REFERENCES parsed_candidate_data(id) ON DELETE CASCADE,
    strength TEXT
);
CREATE TABLE parsed_companies (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    candidate_id UUID NOT NULL REFERENCES parsed_candidate_data(id) ON DELETE CASCADE,
    company_name TEXT,
    is_current BOOLEAN DEFAULT FALSE
);
