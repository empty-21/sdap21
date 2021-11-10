-- audit
CREATE TABLE "audit" (
    id serial PRIMARY KEY,
    param VARCHAR (255) not null,
    url VARCHAR (255) not null,
    runAt TIMESTAMP WITH TIME ZONE,
    createdAt TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);