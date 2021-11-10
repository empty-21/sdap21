-- [2021-08-18 17:34:49]
-- template생성에 따른 behavior정보 binding 용
-- pk.. fk..
CREATE TABLE token_behavior_for_template(
    template_id integer,
    behavior VARCHAR (255),
    updatedat timestamp with time zone,
    createdAt TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (template_id, behavior)
);