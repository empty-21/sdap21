INSERT INTO wallet (userid, account_addr, account_addr_rep) value
 (
     :#${headers.dsl[userid]},
     :#${headers.dsl[account_addr]},
     :#${headers.dsl[account_addr_rep]}
 );