'''
Assignment 4
This is the ChatBot for image classification, via asynchronous connection

This script downloads the image and passes it on to the image message queue

Author: Claudio Fanconi
Student ID: 1155123790
'''

from PIL import Image
from io import BytesIO
from redis import StrictRedis
import json, requests, base64
import os
import telepot

# Constant Variables
PORT = 6379
HOST = 'localhost'
TOKEN = '714729731:AAHtYRM-7dKBC3ZPBpZPFmSQciAl4fL2HDM'

# Defining Redis Message Queue
r = StrictRedis(host= HOST, port= PORT)
bot = telepot.Bot(TOKEN)

# Listening to incoming messages
while True:
        data = r.blpop('download')[1].decode('utf-8')
        data = json.loads(data)

        chat_id = data['chat_id']
        img_name = img_name = str(chat_id) + '.png'

        # if it is a picture, download it
        if 'file_id' in data:
                bot.download_file(data['file_id'], img_name)

        # if it is a URL, request and download it
        if 'url' in data:
                img_data = requests.get(data['url']).content
                with open(img_name, 'wb') as handler:
                        handler.write(img_data)

        # load image, if it exists
        if os.path.isfile(img_name): 
                img = Image.open(img_name)
                buffered = BytesIO()
                img.save(buffered, format="PNG")
                img = base64.b64encode(buffered.getvalue())

                data = {
                'chat_id': chat_id,
                'encoded_image': img.decode('utf-8')
                }

                data = json.dumps(data)
                r.rpush('image', data.encode('utf-8'))

        # Remove image from directory after it has been used
        os.remove(img_name)
