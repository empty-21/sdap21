SELECT  ID
       ,DEFAULTGRP
       ,AUTHGRPNAMEID
       ,authgrpname
       ,TO_CHAR(CREATEDAT,'yyyymmddhh24miss') AS CREATED
FROM "permissin_group"
ORDER BY CREATEDAT DESC
LIMIT :#${headers[rows]}::bigint
OFFSET :#${headers[page]}::bigint;
