UPDATE
  member
SET
  username = :#${body.username},
  usercompany = :#${body.usercompany},
  userdepart = :#${body.userdepart},
  usertel = :#${body.usertel},
  updatedat = CURRENT_TIMESTAMP
WHERE
  userid = :#${headers.userid};