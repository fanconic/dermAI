'''
Assignment 4
This is the ChatBot for image classification, via asynchronous connection

This Skript is multithreated: 
one threat receives the messages and puts them in the download redis message queue
The second threat listens to responses from the predictions redis message queue

Author: Claudio Fanconi
Student ID: 1155123790
'''

import time, json
import telepot
from telepot.loop import MessageLoop
from threading import Thread
from redis import StrictRedis

PORT = 6379
HOST = 'localhost'
TOKEN = '884124386:AAHhAnr0pdXfKRKK2GLfwQXSj_NkRaz7Zmg'

# Defining Redis Message Queue
r = StrictRedis(host= HOST, port= PORT)

# Process Message from user
def thread_receive(msg):
        content_type, _, chat_id = telepot.glance(msg)
        
        
        # Message is Picture, sent it in the download queue
        if content_type == 'photo':
                        data = {
                                'chat_id': chat_id,
                                'file_id': msg['photo'][-1]['file_id']
                        }

                        # Compress to JSON
                        data = json.dumps(data)
                        r.rpush('download', data.encode('utf-8'))


        # message is not a picture sent explanation
        if content_type != 'photo':
                if msg['text'] == '/start':
                        reply = "Hi and welcome to the DermAItoscope!\n It's really simple how it works: Take a picture of the mole you want to have checked and send it in this chat. We'll run it through our sohpisticated algorithms and let you know the result.\nPlease make sure to take a good close up picture with good light conditions.\n We hope your save!"
                else:
                        reply = "Sorry, I don't understand this command... Try sending me a picture of your mole, so that I can classify it!"
                bot.sendMessage(chat_id, reply)

        

# send response to user
def thread_reply():
        while True:
                data = r.blpop('prediction')[1].decode('utf-8')
                data = json.loads(data)
                chat_id = data['chat_id']
                prediction = data['prediction']
                probability = data['probability']

                # Transfrom dict to reply
                reply = ''

                if prediction == 'outlier':
                        reply = "Your Picture does not seem to contain a mole."

                elif prediction == 'malignant':
                        reply = "Your mole has been classified by our algorithm as MALIGNANT with Probability {} %. We advise you to consult a Dermatologist.\nPlease note, that our algorithm is far from perfectly accurate. We hope it's a false prediction!".format(probability)
                
                else:
                        reply = "Your mole has been classified as BENIGN with probability {}%. Please note though, that our algorithm is not yet perfectly accurate.".format(probability)
                bot.sendMessage(chat_id, reply)

# Main function
if __name__ == "__main__":
        # Start threads
        Thread(target= thread_reply).start()
        
        # Provide your bot's token
        bot = telepot.Bot(TOKEN)
        MessageLoop(bot, thread_receive).run_as_thread()
    

        while True:
                time.sleep(10)
