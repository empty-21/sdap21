UPDATE
    CONTRACT
SET
    EXECISERCHOICE  =   :#${headers.dsl[update_contract][execiserchoice]}::json
,   STATUS          =   :#${headers.dsl[update_contract][status]}
,   CONTRACTITEM    =   :#${headers.dsl[update_contract][contractitem]}::json
,   ARCHIVEDID      =   :#${headers.dsl[update_contract][archivedid]}
,   UPDATEDAT       =   CURRENT_TIMESTAMP
WHERE
    CONTRACTID  =   :#${headers.dsl[update_contract][contractid]}
RETURNING *;
