SELECT
    ID
,    TITLE
,   CONTENTS
,   VIEWCOUNT
,   WRITERID
,   UPDATEDAT
,   CREATEDAT
FROM
    NOTIC
WHERE
    ID  =   CAST(:#${headers[id]}    AS  INTEGER)
;