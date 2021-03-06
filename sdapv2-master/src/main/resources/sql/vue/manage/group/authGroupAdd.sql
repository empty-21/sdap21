--
-- INSERT INTO
--     "permissin_group" (
--         defaultgrp,
--         authgrpname,
--         authgrpnameid,
--         updatedAt
--     )
-- VALUES
--     (
--         true,
--         'test_grp',
--         'test_grp',
--         CURRENT_TIMESTAMP
--     );
INSERT INTO
    "permissin_group" (
        defaultgrp,
        authgrpname,
        authgrpnameid,
        viewhost,
        mgrhost,
        viewconso,
        mgrconso,
        viewnetwork,
        mgrnetwork,
        menumgrda,
        viewissue,
        mgrissue,
        viewcontract,
        viewtransfer,
        menumgrpolicy,
        viewbacicpolicy,
        mgrbasicpolicy,
        viewauthgrp,
        mgrauthgrp,
        viewmember,
        mgrmember,
        viewkyc,
        mgrkyc,
        viewnotice,
        mgrnotice,
        viewnotify,
        mgrnotify,
        viewauditlog,
        menublockexplorer,
        updatedAt
    )
VALUES
    (
        :#${body.defaultgrp},
        :#${body.authgrpname},
        :#${body.authgrpnameid},
        :#${body.viewhost},
        :#${body.mgrhost},
        :#${body.viewconso},
        :#${body.mgrconso},
        :#${body.viewnetwork},
        :#${body.mgrnetwork},
        :#${body.menumgrda},
        :#${body.viewissue},
        :#${body.mgrissue},
        :#${body.viewcontract},
        :#${body.viewtransfer},
        :#${body.menumgrpolicy},
        :#${body.viewbacicpolicy},
        :#${body.mgrbasicpolicy},
        :#${body.viewauthgrp},
        :#${body.mgrauthgrp},
        :#${body.viewmember},
        :#${body.mgrmember},
        :#${body.viewkyc},
        :#${body.mgrkyc},
        :#${body.viewnotice},
        :#${body.mgrnotice},
        :#${body.viewnotify},
        :#${body.mgrnotify},
        :#${body.viewauditlog},
        :#${body.menublockexplorer},
        CURRENT_TIMESTAMP
    );