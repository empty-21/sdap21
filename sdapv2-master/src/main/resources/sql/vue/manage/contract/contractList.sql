SELECT
    seq as ID
,   CONTRACTID
,   ARCHIVEDID
,   PARENTID
,   TEMPLATEID
,   SIGNATORIES
,   OBSERVERS
,   EXECISERCHOICE
,   UUID
,   CONTRACTITEM
,   TO_CHAR(CREATEDAT, 'yyyymmddhh24miss')  AS  CREATED
FROM
    "contract"
ORDER BY
    CREATEDAT   DESC
LIMIT :#${headers[rows]}::bigint
OFFSET :#${headers[page]}::bigint;