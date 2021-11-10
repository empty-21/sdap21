--drop table permissin_group;
-- 그룹
CREATE TABLE "permissin_group" (
-- 기본 그룹인지
defaultgrp boolean default false,
authgrpnameid VARCHAR (50) PRIMARY KEY,
authgrpname VARCHAR (50) not null,
updatedAt TIMESTAMP WITH TIME ZONE,
createdAt TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

viewhost boolean DEFAULT true,
mgrhost boolean DEFAULT true,
viewconso boolean DEFAULT true,
mgrconso boolean DEFAULT true,
viewnetwork boolean DEFAULT true,
mgrnetwork boolean DEFAULT true,
menumgrda boolean DEFAULT true,
viewissue boolean DEFAULT true,
mgrissue boolean DEFAULT true,
viewcontract boolean DEFAULT true,
viewtransfer boolean DEFAULT true,
menumgrpolicy boolean DEFAULT true,
viewbacicpolicy boolean DEFAULT true,
mgrbasicpolicy boolean DEFAULT true,
viewauthgrp boolean DEFAULT true,
mgrauthgrp boolean DEFAULT true,
viewmember boolean DEFAULT true,
mgrmember boolean DEFAULT true,
viewkyc boolean DEFAULT true,
mgrkyc boolean DEFAULT true,
viewnotice boolean DEFAULT true,
mgrnotice boolean DEFAULT true,
viewnotify boolean DEFAULT true,
mgrnotify boolean DEFAULT true,
viewauditlog boolean DEFAULT true,
menublockexplorer boolean  DEFAULT true
);