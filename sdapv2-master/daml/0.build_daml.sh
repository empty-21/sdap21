set -e
rm -rf .daml dist

daml build --project-root ./template/ERC20 -o ./dist/ERC20.dar
daml build --project-root ./template/ERC721 -o ./dist/ERC721.dar
daml build --project-root ./template/User -o ./dist/User.dar
daml build --project-root ./template/Wallet -o ./dist/Wallet.dar
daml build --project-root ./template/Service -o ./dist/Service.dar
daml build --project-root ./template/Giftcard -o ./dist/Giftcard.dar