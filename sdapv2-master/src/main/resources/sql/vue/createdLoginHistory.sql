-- 세션 생성하기
INSERT INTO
    session (
        userid,
        token,
        isActive,
        updatedAt,
        createdAt
    )
VALUES
    (
        :#${body.userid},
        :#${headers.token},
        true,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    );