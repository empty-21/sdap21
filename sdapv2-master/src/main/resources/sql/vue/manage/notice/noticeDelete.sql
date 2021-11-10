DELETE
FROM NOTIC
where ID      =    CAST(:#${body[id]} AS INTEGER);

