DELETE
FROM permissin_group
where ID      =    CAST(:#${body[id]} AS INTEGER);