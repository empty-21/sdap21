-- 인증전, 완료, 거절됨, 심사중
CREATE TYPE kyc_stat AS ENUM(
    'UNAUTHORIZED',
    'AUTHORIZED',
    'DENIED',
    'UNDER_EXAMINATION'
);

-- 알수없음,활성화,비활성화,탈퇴,임시제한,재가입,패스워드변경필요
CREATE TYPE user_stat AS ENUM(
    'UNKNOWN',
    'ACTIVATE',
    'DEACTIVATE',
    'DELETED',
    'TEMPLOCK',
    'REQJOIN',
    'REQINITPASSWORD'
);

-- 알수없음, 활성,비활성,삭제됨
CREATE TYPE account_stat AS ENUM(
    'UNKNOWN',
    'ACTIVATE',
    'DEACTIVATE',
    'DELETED'
);

-- 알수없음, 니모닉 시드 또는 파일로 백업, 서버로백업, keyvault 에서 백업
CREATE TYPE key_backup_method AS ENUM(
    'UNKNOWN',
    'PERSONAL',
    'CENTRAL',
    'KEYVALUT'
);

-- 불가능, 가능
CREATE TYPE permissionok AS ENUM('NOTOK', 'OK');
-- choice 
-- 보류
create type choice as (
    choiceName VARCHAR (255),
    -- 실행시킨 사용자
    performer VARCHAR (255),
    createdAt TIMESTAMP WITH TIME ZONE
);

-- -- permission
-- create type permission as (
--     viewhost boolean,
--     mgrhost boolean,
--     viewconso boolean,
--     mgrconso boolean,
--     viewnetwork boolean,
--     mgrnetwork boolean,
--     menumgrda boolean,
--     viewissue boolean,
--     mgrissue boolean,
--     viewcontract boolean,
--     viewtransfer boolean,
--     menumgrpolicy boolean,
--     viewbacicpolicy boolean,
--     mgrbasicpolicy boolean,
--     viewauthgrp boolean,
--     mgrauthgrp boolean,
--     viewmember boolean,
--     mgrmember boolean,
--     viewkyc boolean,
--     mgrkyc boolean,
--     viewnotice boolean,
--     mgrnotice boolean,
--     viewnotify boolean,
--     mgrnotify boolean,
--     viewauditlog boolean,
--     menublockexplorer boolean
-- );