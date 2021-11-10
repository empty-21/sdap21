-- 그룹
CREATE TABLE "kyc" (
    id serial PRIMARY KEY,
    userid VARCHAR (50) not null,
    filename1 VARCHAR (50) not null,
    filename2 VARCHAR (50) not null,
    approveyn kyc_stat not null default 'UNDER_EXAMINATION' :: kyc_stat,
    updatedAt TIMESTAMP WITH TIME ZONE,
    createdAt TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);