# SocialNetwork

## Swagger
Yaml представление Swagger OpenAPI документации, с кратким описанием запросов содержится в файле `api-docs.yaml`.
После запуска приложения/docker-compose, swagger доступен по ссылке: http://localhost:8189/swagger-ui/index.html#/


## Docker

Образ Java-части проекта собрана [здесь](https://hub.docker.com/repository/docker/kattsyn/socialnet_back_java/general).
В файле `docker-compose.yml` собранный докер-компоуз с некоторыми параметрами окружения, который остается лишь запустить и можно проверять работоспособность.


## База данных

База данных изначально содержит:
- 2 роли - User и Admin.
