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
    :#${headers.dsl[contract][contractid]},
    :#${headers.dsl[contract][templateid]},
    :#${headers.dsl[contract][uuid]},
    :#${headers.dsl[contract][status]},
    :#${headers.dsl[contract][contractitem]}::jsonb,
    :#${headers.dsl[contract][signatories]}::jsonb,
    :#${headers.dsl[contract][observers]}::jsonb,
    :#${headers.dsl[contract][execiserchoice]}::jsonb,
    :#${headers.dsl[contract][parentid]},
    :#${headers.dsl[contract][archivedid]},
    CURRENT_TIMESTAMP
)
;