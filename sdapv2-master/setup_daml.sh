curl --location --request POST 'http://localhost:7575/v1/create' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwczovL2RhbWwuY29tL2xlZGdlci1hcGkiOnsibGVkZ2VySWQiOiJkYXBMZWRnZXIiLCJhcHBsaWNhdGlvbklkIjoiREFNTCIsImFjdEFzIjpbImRhcF9vcGVyYXRvciJdfX0.X_gOGfo4HSVOKCEWrzKJIdoZQg3HhE0DcA8Lg5xahAU' \
--header 'Content-Type: application/json' \
--data-raw '{
    "templateId": "Wallet:Wallet",
    "payload": {
        "name": "dap_operator wallet",
        "address":"0x0c630bf770a9f7a50813044ce7e9193a3ca32df2",
        "services":[],
        "paused":true,
        "pausable":true,
        "owner":"dap_operator",
        "operator":"dap_operator"
    }
}'

curl --location --request POST 'http://localhost:7575/v1/create' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwczovL2RhbWwuY29tL2xlZGdlci1hcGkiOnsibGVkZ2VySWQiOiJkYXBMZWRnZXIiLCJhcHBsaWNhdGlvbklkIjoiREFNTCIsImFjdEFzIjpbImhhbmNvbXdpdGgiXX19.0tY5jDrVeWeYzA1lybk7_X-p_tfuCepNydQIKvo_tB4' \
--header 'Content-Type: application/json' \
--data-raw '{
    "templateId": "Wallet:Wallet",
    "payload": {
        "name": "hancomwith wallet",
        "address":"0x18b913ea9e7883a7558b2be7f0038fbb3966cb98",
        "services":[],
        "paused":true,
        "pausable":true,
        "owner":"hancomwith",
        "operator":"dap_operator"
    }
}'


curl --location --request POST 'http://localhost:7575/v1/create' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwczovL2RhbWwuY29tL2xlZGdlci1hcGkiOnsibGVkZ2VySWQiOiJkYXBMZWRnZXIiLCJhcHBsaWNhdGlvbklkIjoiREFNTCIsImFjdEFzIjpbImFsaWNlIl19fQ.6G-BPwB2QcJMBV-ChBAReo7UYPNi0k8Icy2yTwptFqk' \
--header 'Content-Type: application/json' \
--data-raw '{
    "templateId": "Wallet:Wallet",
    "payload": {
        "name": "alice wallet",
        "address":"0x999913ea9e7883a7558b2be7f0038fbb3966cb98",
        "services":[],
        "paused":true,
        "pausable":true,
        "owner":"alice",
        "operator":"dap_operator"
    }
}'

curl --location --request POST 'http://localhost:7575/v1/create' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwczovL2RhbWwuY29tL2xlZGdlci1hcGkiOnsibGVkZ2VySWQiOiJkYXBMZWRnZXIiLCJhcHBsaWNhdGlvbklkIjoiREFNTCIsImFjdEFzIjpbImJvYiJdfX0.-j-xpinji7MknEkiU7XazcXfvOvEfP3OuD7dSOMmX7o' \
--header 'Content-Type: application/json' \
--data-raw '{
    "templateId": "Wallet:Wallet",
    "payload": {
        "name": "bob wallet",
        "address":"0x888813ea9e7883a7558b2be7f0038fbb3966cb98",
        "services":[],
        "paused":true,
        "pausable":true,
        "owner":"bob",
        "operator":"dap_operator"
    }
}'