INSERT
INTO
    PUBLIC.TRANSFER
(
    servicename
,   tokensymbol
,   fromid
,   toid
,   fromaddress
,   toaddress
,   amount
,   operator
,   signatories
,   observers
,   tokenid
,   commitmenthash
,   uuid
,   updatedat
) VALUES (
    :#${headers.dsl.serviceName}
,   :#${headers.dsl.tokenSymbol}
,   :#${headers.dsl.fromId}
,   :#${headers.dsl.toId}
,   :#${headers.dsl.fromAddress}
,   :#${headers.dsl.toAddress}
,   :#${headers.dsl.amount}
,   :#${headers.dsl.operator}
,   :#${headers.dsl.signatories}::jsonb
,   :#${headers.dsl.observers}::jsonb
,   :#${headers.dsl.tokenId}
,   :#${headers.dsl.commitmentHash}
,   :#${headers.dsl.uuid}
,   CURRENT_TIMESTAMP
)
RETURNING id;