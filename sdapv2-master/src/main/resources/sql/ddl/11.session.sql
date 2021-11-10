-- session
CREATE TABLE "session" (
    id serial PRIMARY KEY,
    userid VARCHAR (50) not null,
    -- refresh token
    token text,
    isActive boolean,
    updatedAt TIMESTAMP WITH TIME ZONE,
    createdAt TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);