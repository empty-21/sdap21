-- 알람 상세조회, 발송자 정보는 필요하지 않나?, 목록은 몇개까지 조회?
SELECT
    to_char(updatedat, 'yyyymmddhh24miss') as updated
,   ID
,   FROMID
,   TOID
,   NOTIFYTYPE
,   NOTIMSG
FROM
NOTIFY
WHERE
    ID  =  CAST(:#${headers.id} AS INTEGER)
ORDER BY CREATEDAT DESC
LIMIT 200;