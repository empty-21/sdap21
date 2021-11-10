select
  username,
  usercompany,
  userdepart,
  useremail,
  usertel,
  authgrp
from
  member
where
    userid = :#${headers.userid};