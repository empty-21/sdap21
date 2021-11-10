SELECT id,userid,userstat,authgrp
FROM member
WHERE userid = :#${headers.dsl[userid]};