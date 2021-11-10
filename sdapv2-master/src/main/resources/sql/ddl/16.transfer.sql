-- transfer, Daml의 플로우를 저장하기위한 entity
-- drop table transfer;
CREATE TABLE "transfer" (
    id serial PRIMARY KEY,
    serviceName VARCHAR(50) not null,
    tokenSymbol VARCHAR(50) not null,
    fromid VARCHAR(50),
    toid VARCHAR(50),
    fromAddress VARCHAR(255) not null,
    toAddress VARCHAR(255) not null,
    amount VARCHAR(255) not null,
    operator VARCHAR(255) not null,
    signatories jsonb,
    observers jsonb,
    tokenID integer,
    commitmentHash VARCHAR(255),
    uuid VARCHAR(255),
    updatedAt TIMESTAMP WITH TIME ZONE,
    createdAt TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);