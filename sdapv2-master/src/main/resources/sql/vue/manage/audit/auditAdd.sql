-- TODO
-- [2021-08-24 13:18:50] 구현
INSERT INTO
    audit (
        PARAM,
        URL,
        runat
    )
VALUES
    (
        :#${headers.param},
        :#${headers.url},
        CURRENT_TIMESTAMP
    );