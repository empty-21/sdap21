UPDATE
    NOTIC
SET VIEWCOUNT   =   VIEWCOUNT   +   1
WHERE
    ID  =   CAST(:#${headers[id]}    AS  INTEGER)