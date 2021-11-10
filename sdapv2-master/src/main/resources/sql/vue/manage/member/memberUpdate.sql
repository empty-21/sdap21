UPDATE
    "member"
SET
    USERNAME    =   :#${body.username}
--,   USEREMAIL   =   :#${body.useremail},
,   USERCOMPANY =   :#${body.usercompany}
,   USERDEPART  =   :#${body.userdepart}
,   USERTEL     =   :#${body.usertel}
,   UPDATEDAT   =   CURRENT_TIMESTAMP
WHERE
    USERID  =   :#${body.userid}
;