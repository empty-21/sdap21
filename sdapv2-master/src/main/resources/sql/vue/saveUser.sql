INSERT INTO
    member (
        userid,
        secret,
        username,
        usercompany,
        userdepart,
        useremail,
        usertel,
        authgrp,
        updatedat
    )
VALUES
    (
        :#${body.userid},
        crypt(
            :#${body.secret},
            gen_salt('bf')
        ),
        :#${body.username},
        :#${body.usercompany},
        :#${body.userdepart},
        :#${body.useremail},
        :#${body.usertel},
        :#${body.authgrp},
        CURRENT_TIMESTAMP
    );