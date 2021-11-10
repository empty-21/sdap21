-- notic
CREATE TABLE "notic" (
    id serial PRIMARY KEY,
    title VARCHAR (100) not null,
    contents text,
    viewcount integer default 0,
    filename VARCHAR (255),
    writerid VARCHAR(50),
    updatedAt TIMESTAMP WITH TIME ZONE,
    createdAt TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);