--drop table token_template;
CREATE TABLE "token_template" (
    id uuid DEFAULT uuid_generate_v4(),
    template_name VARCHAR (50) not null,
    token_base_id VARCHAR (36)  not null,
    token_behavior_id VARCHAR (36)  not null,
    template_creator VARCHAR(255),
    updatedAt TIMESTAMP WITH TIME ZONE,
    createdAt TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (template_name, template_creator,token_base_id,token_behavior_id)
);





