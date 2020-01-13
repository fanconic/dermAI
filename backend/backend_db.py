'''
Backend scrip to save credentials of new users in SQLite DB.

Workflow: Android App creates a JSON string, which is then transmitted via Flask to backend. 
The information is then extracted and stored into the SQLite database.

Author: Claudio Fanconi
Email: claudio.fanconi@outlook.com
'''

from flask import Flask, request, jsonify, render_template
import sqlite3
import hashlib

HOSTNAME = '0.0.0.0'
PORT = 8000
DATABASE = '../databases/MyDB.db'

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

@app.route('/login')
def login():
    """
    Login page
    :return: str
    """
    return render_template('login.html')

@app.route('/new_user', methods=['POST', 'GET'])
def new_user():
    """
    Adds new user to DB
    :return: json
    """
    if request.method == 'POST':
        json = request.get_json()
        try:
            fname = json['fname']
            lname = json['lname']
            email = json['email']
            pwd = json['pwd']
            age = json['age']
            city = json['city']
            country = json['country']

            conn = create_connection(DATABASE)
            with conn:
                cur = conn.cursor()
                cur.execute("INSERT INTO users (fname,lname,email,pwd,age,city,country) VALUES (?,?,?,?,?,?,?)",(fname,lname,email,pwd,age,city,country))
            
            conn.commit()
            msg = "Record successfully added"

        except:
            conn.rollback()
            msg = "error in insert operation"
      
        finally:
            return "Record successfully added to Database."
            conn.close()

if __name__ == '__main__':
    conn = create_connection(DATABASE)
    initialize_db(conn)
    conn.close()

    app.run(host=HOSTNAME, port=PORT)