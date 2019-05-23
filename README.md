# Install postgres

```
docker pull postgres
mkdir -p $HOME/docker/volumes/postgres
docker run --rm   --name pg-docker -e POSTGRES_PASSWORD=docker -d -p 5432:5432 -v $HOME/docker/volumes/postgres:/var/lib/postgresql/data  postgres
```

# Login and create databases
```
psql -h localhost -U postgres -d postgres

CREATE DATABASE "spring-kotlin-graph_test";
CREATE DATABASE "spring-kotlin-graph_development";
```
