# Database Service
This microservice connects the a flask app with a SQLite database in the backend.
To initialize this properly, one has to initialize a database in this directory.
Follow the steps:
```
$ cd database/
$ sudo apt install sqlite
$ sqlite3 MyDB.db ""
```

## Endpoints
### NEW_USER [POST]
```
\new_user
```
This endpoint request will register a new user in the DB. Additionally, it checks if the user already exists.

### LOGIN [GET]
```
\login
```
This endpoint request will check if the user has put in the right credentials and can be logged in.
