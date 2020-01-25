'''
Backend scrip to save credentials of new users in SQLite DB.

Workflow: Android App creates a JSON string, which is then transmitted via Flask to backend. 
The information is then extracted and stored into the SQLite database.

Author: Claudio Fanconi
Email: claudio.fanconi@outlook.com
'''

from flask import Flask, request, jsonify, render_template
import json
import sqlite3
import hashlib

HOSTNAME = '0.0.0.0'
PORT = 5000
DATABASE = './MyDB.db'

# Initialize Flask application
app = Flask(__name__)

def create_connection(db_file):
    """ create a database connection to the SQLite database
        specified by db_file
    :param db_file: database file
    :return: Connection object or None
    """
    conn = None
    try:
        conn = sqlite3.connect(db_file)
    except sqlite3.Error as e:
        print(e)
    return conn

def initialize_db(conn):
    """ Initialize user table
    :param conn: database connection
    """
    with conn:
        cur = conn.cursor()
        cur.execute('CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT,fname TEXT, lname TEXT, email TEXT, pwd TEXT, age INTEGER, city TEXT, country TEXT)')

    conn.commit()

# root
@app.route('/')
def index():
    """
    this is a root dir of my server
    :return: str
    """
    return "hello there %s" % request.remote_addr

@app.route('/new_user', methods=['POST', 'GET'])
def new_user():
    """
    Adds new user to DB
    :return: json
    """
    msg = {"status":"UNKNOWN"}
    if request.method == 'POST':
        received = request.get_json()
        try:
            fname = received['fname']
            lname = received['lname']
            email = received['email']
            pwd = received['pwd']
            age = received['age']
            city = received['city']
            country = received['country']

            conn = create_connection(DATABASE)
            with conn:
                cur = conn.cursor()
                cur.execute('SELECT * FROM users WHERE (fname=? AND lname=? AND email=?)', (fname, lname, email))
                entry = cur.fetchone()
                
                if entry is None:
                    msg = {"status":"ALREADY EXISTS"}
                else:
                    cur.execute("INSERT INTO users (fname,lname,email,pwd,age,city,country) VALUES (?,?,?,?,?,?,?)",(fname,lname,email,pwd,age,city,country))
                    msg = {"status":"OK"}

            conn.commit()

        except:
            conn.rollback()
            msg = {"status":"ERROR"}
      
        finally:
            return json.dumps(msg)
            conn.close()

@app.route('/login', methods=['POST', 'GET'])
def login():
    """
    Verifies Login
    :return: json
    """
    msg = {"status":"UNKNOWN"}
    if request.method == 'POST':
        received = request.get_json()
        try:
            email = received['email']
            pwd = received['pwd']
            conn = create_connection(DATABASE)
            with conn:
                cur = conn.cursor()
                cur.execute('SELECT * FROM users WHERE (email=? AND pwd=?)', (email, pwd))
                entry = cur.fetchone()
                
                if entry is None:
                    msg = {"status":"NOT CORRECT"}
                else:
                    msg = {"status":"OK"}
            conn.commit()

        except:
            conn.rollback()
            msg = {"status":"ERROR"}
      
        finally:
            return json.dumps(msg)
            conn.close()

if __name__ == '__main__':
    conn = create_connection(DATABASE)
    initialize_db(conn)
    conn.close()

    app.run(host=HOSTNAME, port=PORT)