'''
Assignment 4
This is the ChatBot for image classification, via asynchronous connection

This script predicts an image based on the pretrained inception v3 model and passess it on to the predictions redis message queue.

Author: Claudio Fanconi
Student ID: 1155123790
'''
from redis import StrictRedis
from keras.models import model_from_json
from keras.optimizers import Adam
import json, base64, requests
from io import BytesIO
from PIL import Image
import numpy as np
import cv2
from keras.preprocessing.image import ImageDataGenerator
import gc


# Constant Variables
PORT = 6379
HOST = 'localhost'
THRESHOLD = 0.1
BATCH_SIZE = 32

# Using original generator
generator = ImageDataGenerator(
        zoom_range=0.5,  # set range for random zoom
        rotation_range = 45,
        horizontal_flip=True,  # randomly flip images
        vertical_flip=True,  # randomly flip images
    )

# load Autoencoder
json_file = open('./Autoencoder/autoencoder.json', 'r')
loaded_model_json = json_file.read()
json_file.close()

autoencoder = model_from_json(loaded_model_json)
# load weights into new model
autoencoder.load_weights('./Autoencoder/autoencoder.h5')
print("Loaded autoencoder from disk")

optimizer = Adam()
autoencoder.summary()
autoencoder.compile(optimizer = optimizer , loss = "binary_crossentropy")

# load resnet50
json_file = open('./CNN/densenet169.json', 'r')
loaded_model_json = json_file.read()
json_file.close()

model = model_from_json(loaded_model_json)
# load weights into new model
model.load_weights('./CNN/densenet169.h5')
print("Loaded DenseNet169 from Disk")

optimizer = Adam()
model.summary()
model.compile(optimizer = optimizer , loss = "binary_crossentropy", metrics=['accuracy'])

# Defining Redis Message Queue
r = StrictRedis(host= HOST, port= PORT)

# Listening to incoming messages
while True:
        data = r.blpop('image')[1].decode('utf-8')
        data = json.loads(data)
       
        chat_id = data['chat_id']
        encoded_image = data['encoded_image']
        img = base64.b64decode(encoded_image)
        img = Image.open(BytesIO(img)).convert("RGB")

        # resize image
        img = img.resize((224, 224))
        img = cv2.cvtColor(np.float32(img), cv2.COLOR_BGR2RGB)
        img = cv2.addWeighted (img,4, cv2.GaussianBlur( img, (0,0) , 10) ,-4 ,128) 
        img = np.array(img)
        img = img/255.
        img =np.reshape(img, (1,224,224,3))
        
        # Check if picture is an outliar
        decoded_img = autoencoder.predict(img)
        mse = np.mean((img - decoded_img)**2)
        print(mse)
        if mse> THRESHOLD:
            prediction = 'outlier'
            probability = 0

        else:
            img = img*255.
            # Predict with TTA
            tta_steps = 10
            predictions = []
            for i in range(tta_steps):
                preds = model.predict_generator(generator.flow(img, batch_size=BATCH_SIZE, shuffle=False),
                                                steps = len(img)/BATCH_SIZE)
                
                predictions.append(preds)
                gc.collect() 
            y_proba = np.mean(predictions, axis=0)

            y_pred = np.argmax(y_proba, axis= 1)
            print(y_proba, y_pred[0])

            probability = y_proba[0][y_pred[0]]*100
            prediction = 'benign' 
            if y_pred == 1:
                prediction = 'malignant'
        
        # prepare data to be sent back
        data = {
            'chat_id': chat_id,
            'prediction': prediction,
            'probability': str(probability)
        }

        data = json.dumps(data)
        r.rpush('prediction', data.encode('utf-8'))