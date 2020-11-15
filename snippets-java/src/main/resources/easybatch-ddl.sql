CREATE TABLE easybatch_job_record
(
    id              varchar(36)
        CONSTRAINT easybatch_job_record_pk
            PRIMARY KEY,
    job_name        varchar(50) NOT NULL,
    job_start_time  timestamp,
    job_end_time    timestamp,
    job_status      varchar(50),
    batch_id        varchar(36),
    data_start_time timestamp,
    data_end_time   timestamp,
    read_count      bigint,
    write_count     bigint,
    filter_count    bigint,
    error_count     bigint,
    job_parameters  text,
    last_error      text,
    created_date    timestamp
);

CREATE TABLE easybatch_job_time
(
    job_name        varchar(50) NOT NULL
        CONSTRAINT easybatch_job_time_pk
            PRIMARY KEY,
    data_start_time timestamp
);
