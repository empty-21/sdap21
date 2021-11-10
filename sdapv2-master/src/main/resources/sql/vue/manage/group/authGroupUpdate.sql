UPDATE
    "permissin_group"
SET
    DEFAULTGRP          =   :#${body.defaultgrp}
,   AUTHGRPNAME         =   :#${body.authgrpname}
,   AUTHGRPNAMEID       =   :#${body.authgrpnameid}
,   VIEWHOST            =   :#${body.viewhost}
,   MGRHOST             =   :#${body.mgrhost}
,   VIEWCONSO           =   :#${body.viewconso}
,   MGRCONSO            =   :#${body.mgrconso}
,   VIEWNETWORK         =   :#${body.viewnetwork}
,   MGRNETWORK          =   :#${body.mgrnetwork}
,   MENUMGRDA           =   :#${body.menumgrda}
,   VIEWISSUE           =   :#${body.viewissue}
,   MGRISSUE            =   :#${body.mgrissue}
,   VIEWCONTRACT        =   :#${body.viewcontract}
,   VIEWTRANSFER        =   :#${body.viewtransfer}
,   MENUMGRPOLICY       =   :#${body.menumgrpolicy}
,   VIEWBACICPOLICY     =   :#${body.viewbacicpolicy}
,   MGRBASICPOLICY      =   :#${body.mgrbasicpolicy}
,   VIEWAUTHGRP         =   :#${body.viewauthgrp}
,   MGRAUTHGRP          =   :#${body.mgrauthgrp}
,   VIEWMEMBER          =   :#${body.viewmember}
,   MGRMEMBER           =   :#${body.mgrmember}
,   VIEWKYC             =   :#${body.viewkyc}
,   MGRKYC              =   :#${body.mgrkyc}
,   VIEWNOTICE          =   :#${body.viewnotice}
,   MGRNOTICE           =   :#${body.mgrnotice}
,   VIEWNOTIFY          =   :#${body.viewnotify}
,   MGRNOTIFY           =   :#${body.mgrnotify}
,   VIEWAUDITLOG        =   :#${body.viewauditlog}
,   MENUBLOCKEXPLORER   =   :#${body.menublockexplorer}
,   UPDATEDAT           =   CURRENT_TIMESTAMP
WHERE
    ID      =    CAST(:#${headers.id} AS INTEGER);
;