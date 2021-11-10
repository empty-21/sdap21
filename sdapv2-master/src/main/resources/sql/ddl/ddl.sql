CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";
drop table audit;
drop table contract;
drop table kyc;
drop table member;
drop table notic;
drop table notify;
drop table permissin_group;
drop table policy;
drop table session;
drop table token_base;
drop table token_behavior;
drop table token_behavior_for_template;
drop table token_template;
drop table transfer;
drop table wallet;

CREATE TYPE kyc_stat AS ENUM(
    'UNAUTHORIZED',
    'AUTHORIZED',
    'DENIED',
    'UNDER_EXAMINATION'
);

CREATE TYPE user_stat AS ENUM(
    'UNKNOWN',
    'ACTIVATE',
    'DEACTIVATE',
    'DELETED',
    'TEMPLOCK',
    'REQJOIN',
    'REQINITPASSWORD'
);

CREATE TYPE account_stat AS ENUM(
    'UNKNOWN',
    'ACTIVATE',
    'DEACTIVATE',
    'DELETED'
);

CREATE TYPE key_backup_method AS ENUM(
    'UNKNOWN',
    'PERSONAL',
    'CENTRAL',
    'KEYVALUT'
);

CREATE TYPE permissionok AS ENUM('NOTOK', 'OK');
-- choice 
-- 보류
create type choice as (
    choiceName VARCHAR (255),
    -- 실행시킨 사용자
    performer VARCHAR (255),
    createdAt TIMESTAMP WITH TIME ZONE
);
-- audit
CREATE TABLE "audit" (
    id serial PRIMARY KEY,
    param VARCHAR (255) not null,
    url VARCHAR (255) not null,
    runAt TIMESTAMP WITH TIME ZONE,
    createdAt TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE "contract" (
    -- contractid를 키로 만든다
    seq serial,
    contractId VARCHAR(255) PRIMARY KEY,
    -- 같은 template에서 변경되었을때 아카이브된 contractid
    archivedId VARCHAR(255) default '',
    -- 다른 template의 choice를 실행하여 만들어진경우 만든 contractid에 해당
    parentId VARCHAR(255),
    -- template의 이름
    templateId VARCHAR(255),
    -- signatories 서명자 목록
    signatories jsonb,
    -- observers 서명자 목록
    observers jsonb,
    -- 템플릿에서 실행된 choice 목록
    execiserChoice jsonb,
    -- uuid
    uuid VARCHAR(255),
    -- 현재 상태 1: active, 0: archived
    status integer,
    -- daml 에 저장된 데이터 모두 넣는부분
    contractItem jsonb,
    --text,
    updatedAt TIMESTAMP WITH TIME ZONE,
    createdAt TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
-- 그룹
CREATE TABLE "kyc" (
    id serial PRIMARY KEY,
    userid VARCHAR (50) not null,
    filename1 VARCHAR (50) not null,
    filename2 VARCHAR (50) not null,
    approveyn kyc_stat not null default 'UNDER_EXAMINATION'::kyc_stat,
    updatedAt TIMESTAMP WITH TIME ZONE,
    createdAt TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
-- 사용자 정보
CREATE TABLE member (
    id serial PRIMARY KEY,
    -- 기본 사용자 유무 ( true일 경우엔 삭제 못하게...)
    defaultuser BOOL DEFAULT true,
    -- 사용자 id
    userid VARCHAR (60) not null,
    -- 비밀번호
    secret TEXT not null,
    -- 사용자 이름
    username VARCHAR (50) not null,
    -- 사용자 상태
    userStat user_stat default 'ACTIVATE'::user_stat,
    -- 사용자 회사명
    usercompany VARCHAR (50),
    -- 사용자 부서
    userdepart VARCHAR (50),
    -- 사용자 이메일
    useremail VARCHAR (255) UNIQUE NOT NULL,
    -- 사용자 전화번호
    usertel VARCHAR (50),
    fidokey VARCHAR (50),
    -- 사용자 그룹
    authgrp VARCHAR (50),
    -- 로그인횟수
    logincnt Integer default 0,
    -- 로그인실패
    loginfailcnt Integer default 0,
    -- 최근 접속 IP주소
    lastloginedip VARCHAR (50),
    -- object[]
    kyc integer Array,
    -- 다중 지갑을 소유 할수 있음 (1:N)
    --wallets  integer Array,
    -- 사용사 사이 관계 (1:N)
    addressBook integer Array,
    -- kyc 인증 상태
    kycvalidyn kyc_stat default 'UNAUTHORIZED'::kyc_stat,
    -- 최근 접속일
    lastlogindt TIMESTAMP WITH TIME ZONE,
    -- 갱신일
    updatedAt TIMESTAMP WITH TIME ZONE,
    -- 생성일
    createdAt TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
-- notic
CREATE TABLE "notic" (
    id serial PRIMARY KEY,
    title VARCHAR (100) not null,
    contents text,
    viewcount integer default 0,
    filename VARCHAR (255),
    writerid VARCHAR(50),
    updatedAt TIMESTAMP WITH TIME ZONE,
    createdAt TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE "notify" (
    id serial PRIMARY KEY,
    parentid integer,
    fromid VARCHAR(50) not null,
    toid VARCHAR(50) not null,
    notifytype VARCHAR(50) not null DEFAULT 'default',
    notimsg text not null,
    readmark integer default 0,
    updatedAt TIMESTAMP WITH TIME ZONE,
    createdAt TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
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
    menublockexplorer boolean DEFAULT true
);
-- policy
CREATE TABLE "policy" (
    id serial PRIMARY KEY,
    pwdrulemin integer default 6 not null,
    pwdrulemax integer default 12 not null,
    pwdruleperiod integer default 90 not null,
    pwdrulefailnotlogin integer default 1 not null,
    pwdrulefailcntlimit integer default 5 not null,
    pwdruleanywhereable integer default 0 not null,
    pwdruleincnum integer default 1 not null,
    pwdruleincspecial integer default 1 not null,
    pwdruleincupper integer default 1 not null,
    pwdruleinclower integer default 1 not null,
    sstimeoutable integer default 1 not null,
    sstimeoutmin integer default 1 not null,
    dapconsoid integer default 1 not null,
    dapprivacygrpid integer default 1 not null,
    ctrlimitwon integer default 100000 not null,
    updatedAt TIMESTAMP WITH TIME ZONE,
    createdAt TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
-- session
CREATE TABLE "session" (
    id serial PRIMARY KEY,
    userid VARCHAR (50) not null,
    -- refresh token
    token text,
    isActive boolean,
    updatedAt TIMESTAMP WITH TIME ZONE,
    createdAt TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
-- token base
--drop table token_base;
CREATE TABLE "token_base" (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    "name" VARCHAR (50) not null,
    -- fungible = true (FT) / not true (NTF)
    fungible boolean,
    -- nonfungible = true (NFT) / not true (TF)
    nonfungible boolean,
    updatedAt TIMESTAMP WITH TIME ZONE,
    createdAt TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
-- [2021-08-18 17:34:49]
-- template생성에 따른 behavior정보 binding 용
-- pk.. fk..
CREATE TABLE token_behavior_for_template(
    template_id integer,
    behavior VARCHAR (255),
    updatedat timestamp with time zone,
    createdAt TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (template_id, behavior)
);
--drop table token_behavior;
-- token_behavior
CREATE TABLE token_behavior (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    behavior VARCHAR (50) not null,
    formula VARCHAR (255) not null,
    -- fungible = true (FT) / not true (NTF)
    fungible boolean default false,
    -- nonfungible = true (NFT) / not true (TF)
    nonfungible boolean default false,
    updatedAt TIMESTAMP WITH TIME ZONE,
    createdAt TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
--drop table token_template;
CREATE TABLE token_template (
    id uuid DEFAULT uuid_generate_v4(),
    template_name VARCHAR (50) not null,
    token_base_id VARCHAR (36) not null,
    token_behavior_id VARCHAR (36) not null,
    template_creator VARCHAR(255),
    updatedAt TIMESTAMP WITH TIME ZONE,
    createdAt TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (
        template_name,
        template_creator,
        token_base_id,
        token_behavior_id
    )
);
-- transfer, Daml의 플로우를 저장하기위한 entity
-- drop table transfer;
CREATE TABLE "transfer" (
    id serial PRIMARY KEY,
    serviceName VARCHAR(50) not null,
    tokenSymbol VARCHAR(50) not null,
    fromid VARCHAR(50),
    toid VARCHAR(50),
    fromAddress VARCHAR(255) not null,
    toAddress VARCHAR(255) not null,
    amount VARCHAR(255) not null,
    operator VARCHAR(255) not null,
    signatories jsonb,
    observers jsonb,
    tokenID integer,
    commitmentHash VARCHAR(255),
    uuid VARCHAR(255),
    updatedAt TIMESTAMP WITH TIME ZONE,
    createdAt TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE "wallet" (
    id serial PRIMARY KEY,
    -- member의 id필드와 맵핑
    account_id integer,
    consoid numeric,
    -- 월렛 User 아이디
    userid VARCHAR (255) not null,
    -- 월렛 사용자 아이디
    -- account_id VARCHAR (255) not null,
    -- wallet address
    account_addr VARCHAR (255),
    -- 대표 지갑 여부  : member로 옴겨야 할지 부분은 고려
    account_addr_rep boolean default true,
    account_stat account_stat default 'ACTIVATE'::account_stat,
    -- wallet backup method
    keybackupmethod key_backup_method default 'PERSONAL'::key_backup_method,
    updatedAt TIMESTAMP WITH TIME ZONE,
    createdAt TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
INSERT INTO permissin_group (
        DEFAULTGRP,
        AUTHGRPNAME,
        AUTHGRPNAMEID,
        UPDATEDAT
    )
VALUES (TRUE, '오퍼레이터', 'DAP_OPERATOR', CURRENT_TIMESTAMP),
    (TRUE, '옵저버', 'DAP_OBSERVER', CURRENT_TIMESTAMP),
    (TRUE, '자산관리자', 'DAP_MANAGER', CURRENT_TIMESTAMP),
    (TRUE, '일반사용자', 'COMMON_USER', CURRENT_TIMESTAMP),
    (
        TRUE,
        '일반가맹점',
        'COMMON_FRANCHISEE',
        CURRENT_TIMESTAMP
    );
INSERT INTO POLICY (
        PWDRULEMIN,
        PWDRULEMAX,
        PWDRULEPERIOD,
        PWDRULEFAILNOTLOGIN,
        PWDRULEFAILCNTLIMIT,
        PWDRULEANYWHEREABLE,
        PWDRULEINCNUM,
        PWDRULEINCSPECIAL,
        PWDRULEINCUPPER,
        PWDRULEINCLOWER,
        SSTIMEOUTABLE,
        SSTIMEOUTMIN,
        DAPCONSOID,
        DAPPRIVACYGRPID,
        CTRLIMITWON
    )
VALUES (
        6,
        12,
        90,
        1,
        5,
        0,
        1,
        1,
        1,
        1,
        1,
        1,
        1,
        1,
        100000
    )
INSERT INTO MEMBER (
        DEFAULTUSER,
        USERID,
        SECRET,
        USEREMAIL,
        USERNAME,
        AUTHGRP,
        CREATEDAT
    )
VALUES (
        TRUE,
        'bob',
        crypt('111111', gen_salt('bf')),
        'bob@email.com',
        '초기관리자',
        'DAP_OPERATOR',
        CURRENT_TIMESTAMP
    ),
    (
        TRUE,
        'dap_operator',
        crypt('111111', gen_salt('bf')),
        'operator@email.com',
        '초기관리자',
        'DAP_OPERATOR',
        CURRENT_TIMESTAMP
    ),
    (
        TRUE,
        'hancomwith',
        crypt('111111', gen_salt('bf')),
        'hancomwith@email.com',
        '사용자',
        'DAP_OPERATOR',
        CURRENT_TIMESTAMP
    ),
    (
        TRUE,
        'dap_observer',
        crypt('111111', gen_salt('bf')),
        'observer@email.com',
        '관찰담당자',
        'DAP_OBSERVER',
        CURRENT_TIMESTAMP
    ),
    (
        TRUE,
        'alice',
        crypt('111111', gen_salt('bf')),
        'alice@email.com',
        '사용자',
        'DAP_OPERATOR',
        CURRENT_TIMESTAMP
    );
INSERT INTO WALLET (
        USERID,
        ACCOUNT_ADDR,
        ACCOUNT_ADDR_REP,
        UPDATEDAT
    )
VALUES (
        'dap_operator',
        '0x0c630bf770a9f7a50813044ce7e9193a3ca32df2',
        TRUE,
        CURRENT_TIMESTAMP
    );
INSERT INTO WALLET (
        USERID,
        ACCOUNT_ADDR,
        ACCOUNT_ADDR_REP,
        UPDATEDAT
    )
VALUES (
        'bob',
        '0x888813ea9e7883a7558b2be7f0038fbb3966cb98',
        TRUE,
        CURRENT_TIMESTAMP
    );
INSERT INTO WALLET (
        USERID,
        ACCOUNT_ADDR,
        ACCOUNT_ADDR_REP,
        UPDATEDAT
    )
VALUES (
        'alice',
        '0x999913ea9e7883a7558b2be7f0038fbb3966cb98',
        TRUE,
        CURRENT_TIMESTAMP
    );
INSERT INTO TOKEN_BASE (NAME, FUNGIBLE, NONFUNGIBLE)
VALUES ('Fungible Token', TRUE, FALSE),
    ('Non Fungible Token', FALSE, TRUE),
    ('GiftCard Token', TRUE, TRUE);
INSERT INTO TOKEN_BASE (NAME, FUNGIBLE, NONFUNGIBLE)
VALUES ('Fungible Token', TRUE, FALSE),
    ('Non Fungible Token', FALSE, TRUE),
    ('GiftCard Token', TRUE, TRUE);
INSERT INTO TOKEN_BEHAVIOR (
        BEHAVIOR,
        FORMULA,
        FUNGIBLE,
        NONFUNGIBLE
    )
VALUES ('dividable', 'd', TRUE, FALSE),
    ('pausable', 'p', TRUE, TRUE),
    ('mintable', 'm', TRUE, TRUE),
    ('burnable', 'b', TRUE, TRUE),
    ('distributable', '-', TRUE, FALSE),
    ('transferable', 't', TRUE, TRUE),
    ('delegable', 'g', TRUE, FALSE);