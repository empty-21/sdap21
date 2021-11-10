select
    crypt(:#${body.secret}, secret) = secret validsecret,
    userstat,
    userid,
    username,
    usercompany,
    useremail,
    authgrp
from
    "public"."member"
where
    userid = :#${body.userid};