UPDATE
    notify
SET
    readmark = 1
WHERE
    ID  =   CAST(:#${body[id]}    AS  INTEGER);