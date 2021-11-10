INSERT INTO
    notic (
        title,
        contents,
        writerid,
        updatedAt
    )
VALUES
    (
        :#${body.title},
        :#${body.contents},
        :#${headers[userid]},
        CURRENT_TIMESTAMP
    );