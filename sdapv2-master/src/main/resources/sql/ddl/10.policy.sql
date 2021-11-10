-- policy
CREATE TABLE "policy" (
    id serial PRIMARY KEY,
    pwdrulemin integer default 6 not null,
    pwdrulemax integer default 12 not null,
    pwdruleperiod integer default 90 not null,
    pwdrulefailnotlogin integer default 1 not null,
    pwdrulefailcntlimit integer default 5 not null,
    pwdruleanywhereable integer default 0 not null,
    pwdruleincnum integer default 1 not null,
    pwdruleincspecial integer default 1 not null,
    pwdruleincupper integer default 1 not null,
    pwdruleinclower integer default 1 not null,
    sstimeoutable integer default 1 not null,
    sstimeoutmin integer default 1 not null,
    dapconsoid integer default 1 not null,
    dapprivacygrpid integer default 1 not null,
    ctrlimitwon integer default 100000 not null,
    updatedAt TIMESTAMP WITH TIME ZONE,
    createdAt TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);