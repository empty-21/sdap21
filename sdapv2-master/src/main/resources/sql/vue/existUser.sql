select
    count(*) as rows
from
    "public"."member"
where
    userid = :#${body.userid}
    or useremail = :#${body.useremail};