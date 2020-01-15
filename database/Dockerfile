FROM nouchka/sqlite3
RUN apt-get update -y
RUN apt-get install -y python-pip python-dev build-essential
RUN mkdir /db
RUN sqlite3 /db/MyDB.db
