# killbill-getnet
A [Getnet](https://getnet.com.br) payment plugin for Killbill

## Build

```
​mvn clean install -DskipTests
```

## Installation

```
kpm install_java_plugin getnet --from-source-file target/getnet-plugin-*-SNAPSHOT.jar --destination /var/tmp/bundles
```
Manually import the ddl.sql according to your database from the resources folder to create the necessary local tables for keeping Getnet metadata, such as payment confirmation and credit card tokens.

## Setting-up

*Keep in mind that this plugin was developed using the Hologação environment from Getnet, which is very similar to production BUT a lot different from the Sandbox. If you wish to try before putting in a live, real env, ask Getnet E-commerce support [here](https://developers.getnet.com.br/api#section/Como-comecar/Necessita-ajuda) for your own store Homologação credentials.*

Before starting Kill Bill, be sure to upload the Plugin Config either thru Kaui or via API request to http://127.0.0.1:8080/1.0/kb/tenants/uploadPluginConfig/getnet
```
org.killbill.billing.plugin.getnet.url=https://api-homologacao.getnet.com.br
org.killbill.billing.plugin.getnet.seller_id=<uuid-sellerId>
org.killbill.billing.plugin.getnet.client_id=<uuid-clientId>
org.killbill.billing.plugin.getnet.client_secret=<uuid-clientSecret>
org.killbill.billing.plugin.getnet.verify_card=false
org.killbill.billing.plugin.getnet.softdescriptor=Loja
```
Verify Card property controls if the card being stored on Getnet's vault should have a successful zero dollar auth performed during the tokenization flow. [Check Getnet docs for more details.](https://developers.getnet.com.br/api#tag/Cofre/paths/~1v1~1cards/post)
Softdescriptor property can be set to "0" or not declared to disable soft-descriptor on transactions. At this time, Getnet imposes a limit of 22 chars to the field. The plugin will substring anything you set to 20, being the format "your property value*transaction id".

## How to get started
The flow to create a account, store a card and perform a transaction follows:

1. Create a Kill Bill account for the customer
```
curl -v \
     -X POST \
     -u admin:password \
     -H 'X-Killbill-ApiKey: bob' \
     -H 'X-Killbill-ApiSecret: lazar' \
     -H 'X-Killbill-CreatedBy: tutorial' \
     -H 'Content-Type: application/json' \
     -d '{ "currency": "BRL" }' \
     'http://127.0.0.1:8080/1.0/kb/accounts'
```
This returns the Kill Bill `accountId` in the `Location` header.

2. Use either Kaui or the API to add the credit card to the Getnet vault and store the token back to the database.

You can either tokenize the credit card thru Killbill, by passing all the credit card data - it is **very important** that you supply the externalKey property with ANY unique value in your database.

```
curl -v \
     -X POST \
     -u admin:password \
     -H 'X-Killbill-ApiKey: bob' \
     -H 'X-Killbill-ApiSecret: lazar' \
     -H 'X-Killbill-CreatedBy: tutorial' \
     -H 'Content-Type: application/json' \
     -d '{
	   "externalKey": "123456",
       "pluginName": "killbill-getnet",
       "pluginInfo": {
         "properties": [{
	         "key": "ccFirstName",
	         "value": "ze maria da silva"
          },
          {
	         "key": "ccExpirationMonth",
	         "value": "02"
          },
          {
	         "key": "ccExpirationYear",
	         "value": "2029"
          },
          {
	         "key": "ccNumber",
	         "value": "4111111111111111"
          }
         ]
       }
     }' \
     'http://127.0.0.1:8080/1.0/kb/accounts/<ACCOUNT-ID>/paymentMethods?isDefault=true'
```

3. If you already have the card token from the Getnet vault (because you tokenized thru your store front-end, past payment, etc), you may pass it back to Killbill and the plugin will store in its internal database tables.

```
curl -v \
     -X POST \
     -u admin:password \
     -H 'X-Killbill-ApiKey: bob' \
     -H 'X-Killbill-ApiSecret: lazar' \
     -H 'X-Killbill-CreatedBy: tutorial' \
     -H 'Content-Type: application/json' \
     -d '{
	   "externalKey": "123457",
       "pluginName": "killbill-getnet"
     }' \
     'http://127.0.0.1:8080/1.0/kb/accounts/<ACCOUNT-ID>/paymentMethods?isDefault=false&pluginProperty=cardId%3D<GETNET-VAULT-CARD-TOKEN>'
```

4. You can then trigger payments against that payment method
```
curl -v \
     -X POST \
     -u admin:password \
     -H "X-Killbill-ApiKey: bob" \
     -H "X-Killbill-ApiSecret: lazar" \
     -H "X-Killbill-CreatedBy: tutorial" \
     -H "Content-Type: application/json" \
     --data-binary '{"transactionType":"PURCHASE","amount":"10"}' \
    'http://127.0.0.1:8080/1.0/kb/accounts/<ACCOUNT_ID>/payments'
```

5. You can then obtain information about the payment as follows:
```
curl -v \
     -u admin:password \
     -H "X-Killbill-ApiKey: bob" \
     -H "X-Killbill-ApiSecret: lazar" \
    'http://127.0.0.1:8080/1.0/kb/payments/<PAYMENT_ID>?withPluginInfo=true'
```

6. If you do not want the plugin to be called, you can specify `withPluginInfo=false` as follows:
```
curl -v \
     -u admin:password \
     -H "X-Killbill-ApiKey: bob" \
     -H "X-Killbill-ApiSecret: lazar" \
    'http://127.0.0.1:8080/1.0/kb/payments/<PAYMENT_ID>?withPluginInfo=false'
```
