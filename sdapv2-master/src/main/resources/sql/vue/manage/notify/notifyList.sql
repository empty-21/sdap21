SELECT
    ID
,   PARENTID
,   FROMID
,   TOID
,   NOTIFYTYPE
,   NOTIMSG
,   READMARK
,   UPDATEDAT
,   CREATEDAT
FROM
NOTIFY
ORDER BY
    CREATEDAT   DESC
WHERE
    TOID    =   :#${headers[address]}
OR  FROMID  =   :#${headers[address]}
LIMIT :#${headers[rows]}::bigint
OFFSET :#${headers[page]}::bigint
;