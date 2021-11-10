-- token base
--drop table token_base;
CREATE TABLE "token_base" (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    "name" VARCHAR (50) not null,
    -- fungible = true (FT) / not true (NTF)
    fungible boolean,
    -- nonfungible = true (NFT) / not true (TF)
    nonfungible boolean,
    updatedAt TIMESTAMP WITH TIME ZONE,
    createdAt TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);