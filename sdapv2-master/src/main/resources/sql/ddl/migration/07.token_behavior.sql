INSERT
INTO
    TOKEN_BEHAVIOR
(
    BEHAVIOR
,   FORMULA
,   FUNGIBLE
,   NONFUNGIBLE
) VALUES
    ('dividable', 'd', TRUE, FALSE)
,   ('pausable', 'p', TRUE, TRUE)
,   ('mintable', 'm', TRUE, TRUE)
,   ('burnable', 'b', TRUE, TRUE)
,   ('distributable', '-', TRUE, FALSE)
,   ('transferable', 't', TRUE, TRUE)
,   ('delegable', 'g', TRUE, FALSE)
;