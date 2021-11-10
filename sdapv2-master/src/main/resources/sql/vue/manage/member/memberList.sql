SELECT
    M.DEFAULTUSER
,   M.USERID
,   M.USERNAME
,   M.USERSTAT
,   M.USERCOMPANY
,   M.USERDEPART
,   M.USEREMAIL
,   M.USERTEL
,   M.AUTHGRP
,   M.LOGINCNT
,   M.LOGINFAILCNT
,   M.LASTLOGINEDIP
,   M.LASTLOGINDT
,   M.CREATEDAT
,   G.DEFAULTGRP
,   G.AUTHGRPNAMEID
,   G.AUTHGRPNAME
,   G.VIEWHOST
,   G.MGRHOST
,   G.VIEWCONSO
,   G.MGRCONSO
,   G.VIEWNETWORK
,   G.MGRNETWORK
,   G.MENUMGRDA
,   G.VIEWISSUE
,   G.MGRISSUE
,   G.VIEWCONTRACT
,   G.VIEWTRANSFER
,   G.MENUMGRPOLICY
,   G.VIEWBACICPOLICY
,   G.MGRBASICPOLICY
,   G.VIEWAUTHGRP
,   G.MGRAUTHGRP
,   G.VIEWMEMBER
,   G.MGRMEMBER
,   G.VIEWKYC
,   G.MGRKYC
,   G.VIEWNOTICE
,   G.MGRNOTICE
,   G.VIEWNOTIFY
,   G.MGRNOTIFY
,   G.VIEWAUDITLOG
,   G.MENUBLOCKEXPLORER
,   COALESCE(WALLET.WALLETS, '[]')  AS  WALLETS
FROM
    MEMBER  M
LEFT   JOIN LATERAL
(
        SELECT
            JSON_AGG(JSON_BUILD_OBJECT(
                'wallet_userid', WALLET.USERID,
                'account_addr', WALLET.ACCOUNT_ADDR,
                'created', WALLET.CREATEDAT,
                'account_stat', WALLET.ACCOUNT_STAT))  AS  WALLETS
        FROM
            WALLET
        WHERE
            WALLET.MEMBER_ID    =   M.USERID
    )   WALLET
ON
    TRUE
JOIN    permissin_group G   ON  M.AUTHGRP       =   G.AUTHGRPNAMEID
WHERE
    M.USERSTAT  !=  'DELETED'
ORDER BY
    M.CREATEDAT   DESC
LIMIT :#${headers[rows]}::bigint
OFFSET :#${headers[page]}::bigint
;