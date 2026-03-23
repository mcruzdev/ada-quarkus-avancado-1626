# JWT

## 1. Generate a private key and a public key using OpenSSL

```shell
openssl rsa \
  -pubout \
  -in private_key.pem \
  -out public_key.pem
```

## 2. Generate a PKCS#8 formatted private key from a PEM formatted private key using OpenSSL:

```shell
openssl pkcs8 \
-topk8 \
-inform PEM \
-outform PEM \
-nocrypt \
-in private_key.pem \
-out private_key_pkcs8.pem
```

