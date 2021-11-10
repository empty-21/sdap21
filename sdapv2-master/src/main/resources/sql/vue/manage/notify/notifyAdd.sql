INSERT INTO
    notify (
        fromid,
        toid,
        notimsg
    )
VALUES
    (
        :#${body.fromid},
        :#${body.toid},
        :#${body.notimsg}
    );