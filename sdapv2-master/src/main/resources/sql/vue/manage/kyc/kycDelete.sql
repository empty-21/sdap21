DELETE
FROM "kyc"
where ID      =    CAST(:#${body[id]} AS INTEGER);