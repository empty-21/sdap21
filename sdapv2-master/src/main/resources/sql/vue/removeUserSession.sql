UPDATE
  session
SET
  isactive = false,
  updatedat = CURRENT_TIMESTAMP
WHERE
  token = :#${headers.token};