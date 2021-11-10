--drop table token_behavior;
-- token_behavior
CREATE TABLE "token_behavior" (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    behavior VARCHAR (50) not null,
    formula VARCHAR (255) not null,
    -- fungible = true (FT) / not true (NTF)
    fungible boolean default false,
    -- nonfungible = true (NFT) / not true (TF)
    nonfungible boolean default false,
    updatedAt TIMESTAMP WITH TIME ZONE,
    createdAt TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

