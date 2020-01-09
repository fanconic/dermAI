from flask import Flask, request, jsonify, render_template
import mysql.connector

app = Flask(__name__)
mydb = mysql.connector.connect(
    host="localhost",
    user="fanconic",
    passwd="",
    database="MyDB"
)

print(mydb) 

# Broadcast and port
HOSTNAME = 'localhost'
PORT = 8000

# root
@app.route("/")
def index():
    """
    this is a root dir of my server
    :return: str
    """
    return "hello there %s" % request.remote_addr

# Register new user to DB
@app.route('/new_user', methods=['GET', 'POST'])
def new_user():
    """
    Register new User
    :return: page rendering
    """
    if request.method == "POST":
        details = request.form
        print(details)
        firstName = details['fname']
        lastName = details['lname']
        cur = mydb.cursor()
        cur.execute("INSERT INTO MyUsers(firstName, lastName) VALUES (%s, %s)", (firstName, lastName))
        cur.close()
        return 'success'
    return render_template('new_user.html')


# GET
@app.route('/users/<user>')
def hello_user(user):
    """
    this serves as a demo purpose
    :param user:
    :return: str
    """
    return "What's up %s, you crazy Dawg!" % user

if __name__ == '__main__':
    app.run(host=HOSTNAME, port=PORT)