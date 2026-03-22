# wallet-api

Comando utilizado para gerar o projeto:

```shell
quarkus create app wallet-api -x rest-jackson -x hibernate-orm-panache -x jdbc-mysql
```

Esse comando cria um projeto Quarkus com as seguintes dependências:

- `quarkus-rest-jackson`: Adiciona suporte a serialização e desserialização de JSON.
- `quarkus-hibernate-orm-panache`: Adiciona suporte a Hibernate ORM com Panache, uma API de persistência simplificada.
- `quarkus-jdbc-mysql`: Adiciona suporte a conexão com o banco de dados MySQL.