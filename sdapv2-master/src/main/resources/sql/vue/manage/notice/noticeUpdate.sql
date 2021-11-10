UPDATE
    NOTIC
SET
    title = :#${body.title}
    ,contents = :#${body.contents}
WHERE
    ID  =   CAST(:#${headers[id]}    AS  INTEGER);