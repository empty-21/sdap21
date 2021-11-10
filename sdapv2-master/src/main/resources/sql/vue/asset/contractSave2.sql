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
    :#${headers.dsl[contractid]},
    :#${headers.dsl[templateid]},
    :#${headers.dsl[uuid]},
    :#${headers.dsl[status]},
    :#${headers.dsl[contractitem]}::jsonb,
    :#${headers.dsl[signatories]}::jsonb,
    :#${headers.dsl[observers]}::jsonb,
    :#${headers.dsl[execiserchoice]}::jsonb,
    :#${headers.dsl[parentid]},
    :#${headers.dsl[archivedid]},
    CURRENT_TIMESTAMP
)
RETURNING CONTRACTID;