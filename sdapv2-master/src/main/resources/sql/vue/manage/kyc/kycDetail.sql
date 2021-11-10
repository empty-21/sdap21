SELECT
    ID
,   USERID
,   FILENAME1
,   FILENAME2
,   APPROVEYN
,   TO_CHAR(UPDATEDAT, 'yyyymmddhh24miss')  AS  UPDATED
,   TO_CHAR(CREATEDAT, 'yyyymmddhh24miss')  AS  CREATED
FROM
    "kyc"
WHERE ID      =    CAST(:#${headers[id]} AS INTEGER);