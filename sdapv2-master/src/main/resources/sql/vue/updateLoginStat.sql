UPDATE
  member
SET
  logincnt = logincnt + 1,
  lastlogindt = CURRENT_TIMESTAMP,
  updatedat = CURRENT_TIMESTAMP
WHERE
  userid = :#${body.userid};