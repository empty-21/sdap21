INSERT INTO WALLET ( USERID , ACCOUNT_ADDR , ACCOUNT_ADDR_REP , UPDATEDAT )
VALUES
(
    :#${headers.dsl[userid]},
    :#${headers.dsl[account_addr]},
    :#${headers.dsl[account_addr_rep]},
    CURRENT_TIMESTAMP
);