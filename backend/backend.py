from flask import Flask, request, jsonify
import base64
from keras import layers
from keras_efficientnets import EfficientNetB3
from keras.models import Sequential
import tensorflow as tf
from keras.models import model_from_json
from io import BytesIO
from PIL import Image
import numpy as np

tf.keras.backend.clear_session()

app = Flask(__name__)

THRESHOLD = 0.04

# Prefilter autoencoder model
json_file = open('./models/autoencoder/autoencoder.json', 'r')
loaded_model_json = json_file.read()
json_file.close()
autoencoder = model_from_json(loaded_model_json)
autoencoder.load_weights('./models/autoencoder/autoencoder.h5')
autoencoder.summary()
print("Loaded autoencoder from disk")

# EfficientNetB3 Skin Cancer Model
model = Sequential()
model.add(EfficientNetB3(weights=None, input_shape=(224,224,3), include_top=False))
model.add(layers.GlobalAveragePooling2D())
model.add(layers.BatchNormalization())
model.add(layers.Dense(2, activation= 'softmax'))  
model.load_weights('./models/classifier/efficientnet.h5')
model.summary()
print("Loaded Classifier from Disk")

global graph
graph = tf.get_default_graph() 

# root
@app.route("/")
def index():
    """
    this is a root dir of my server
    :return: str
    """
    return "This is da Mofucking root!"


# GET
@app.route('/users/<user>')
def hello_user(user):
    """
    this serves as a demo purpose
    :param user:
    :return: str
    """
    return "What's up %s, you crazy Dawg!" % user


# POST
@app.route('/api/mole_prediction', methods=['POST'])
def get_mole_prediction():
    """
    predicts mole picture, wether it is bengign or malignant
    :return: json
    """
    json = request.get_json()
    #data = json.loads(data)

    chat_id = str(json['chat_id'])
    encoded_image = json['encoded_image']
    img = base64.b64decode(encoded_image)
    img = Image.open(BytesIO(img)).convert("RGB")
    img.save('./'+chat_id+'.png')

    # resize image
    img = img.resize((224, 224)) 
    img = np.array(img)
    img1 = img / 255.
    img = np.reshape(img, (1,224,224,3))
    img1 =np.reshape(img1, (1,224,224,3))


    # Check if picture is an outliar
    with graph.as_default():
        decoded_img = autoencoder.predict(img1)
    mse = np.mean((img1 - decoded_img)**2)
    print(mse)
    if mse> THRESHOLD:
        prediction = 'outlier'
        probability = 0

    else:
        with graph.as_default():
            y_proba = model.predict(img) 
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
    
    print(data) 
    return jsonify(data)


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)