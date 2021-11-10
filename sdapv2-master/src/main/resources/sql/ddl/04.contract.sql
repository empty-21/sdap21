-- drop table contract ;
-- -- contract
-- select signatories,execiserChoice from contract;
-- insert into contract
-- (signatories,execiserChoice)
-- values(
--     ARRAY['operator','issuer'],
--     '{ "phones1":[ {"type1": "mobile", "phone": "001001"} , {"type2": "fix", "phone": "002002"} ] }'
--     );
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