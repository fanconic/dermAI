# Database Service
This microservice connects the a flask app with a SQLite database in the backend.
To initialize this properly, one has to initialize a database in this directory.
Follow the steps:
```
$ cd database/
$ sudo apt install sqlite
$ sqlite3 MyDB.db ""
```