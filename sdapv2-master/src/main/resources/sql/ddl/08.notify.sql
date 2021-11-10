--drop table notify;
-- notify
CREATE TABLE "notify" (
    id serial PRIMARY KEY,
    parentid integer,
    fromid VARCHAR(50) not null,
    toid VARCHAR(50) not null,
    notifytype VARCHAR(50) not null DEFAULT 'default',
    notimsg text not null,
    readmark integer default 0,
    updatedAt TIMESTAMP WITH TIME ZONE,
    createdAt TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ALTER TABLE notify ALTER COLUMN notifytype set DEFAULT 'default';
 --ALTER TABLE notify ALTER COLUMN notifytype set not null;