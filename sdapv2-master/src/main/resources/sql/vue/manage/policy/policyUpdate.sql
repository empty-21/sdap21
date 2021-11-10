update policy
set
    pwdrulemin = :#${body.pwdrulemin}
,   pwdrulemax = :#${body.pwdrulemax}
,   pwdruleperiod = :#${body.pwdruleperiod}
,   pwdrulefailnotlogin = :#${body.pwdrulefailnotlogin}
,   pwdrulefailcntlimit = :#${body.pwdrulefailcntlimit}
,   pwdruleanywhereable = :#${body.pwdruleanywhereable}
,   pwdruleincnum = :#${body.pwdruleincnum}
,   pwdruleincspecial = :#${body.pwdruleincspecial}
,   pwdruleincupper = :#${body.pwdruleincupper}
,   pwdruleinclower = :#${body.pwdruleinclower}
,   sstimeoutable = :#${body.sstimeoutable}
,   sstimeoutmin = :#${body.sstimeoutmin}
,   dapconsoid = :#${body.dapconsoid}
,   dapprivacygrpid = :#${body.dapprivacygrpid}
,   ctrlimitwon = :#${body.ctrlimitwon}
,   updatedat   =   CURRENT_TIMESTAMP
where
    id  =   cast(:#${headers[id]}    as  integer);