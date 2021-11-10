INSERT
INTO
    CONTRACT
(
    CONTRACTID
,   TEMPLATEID
,   UUID
,   STATUS
,   CONTRACTITEM
,   SIGNATORIES
,   OBSERVERS
,   EXECISERCHOICE
,   PARENTID
,   ARCHIVEDID
,   UPDATEDAT
) VALUES (
    :#${headers.dsl.contractId},
    :#${headers.dsl.templateId},
    :#${headers.dsl.uuid},
    :#${headers.dsl.status},
    :#${headers.dsl.contractItem}::jsonb,
    :#${headers.dsl.signatories}::jsonb,
    :#${headers.dsl.observers}::jsonb,
    :#${headers.dsl.execiserChoice}::jsonb,
    :#${headers.dsl.parentId},
    :#${headers.dsl.archivedId},
    CURRENT_TIMESTAMP
)
RETURNING CONTRACTID;