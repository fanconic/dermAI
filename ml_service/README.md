# Machine Learning Service
This is the backend for the machine learning service.
The classifier was separately trained on kaggle at https://www.kaggle.com/fanconic/skin-cancer-efficientnetb0.
Transfer learning is used on an EfficientNetB0, which is a recently released CNN by Google, whihc achieves high results (AUC 0.92) with only few parameters (~4 mio.).

## Endpoints
### Root [GET]
```
/
```
This end point will return a string with the remote address.

### User [GET]
```
/users/<user>
```
This endpoint will return a string greeting the user.

### Predict [POST]
```
/predict
```
The predict endpoint is POST request, and expects the followowing JSON:
```
{
  chat_id: ... (str)
  encoded_img: ... (Base64 encoded String of image)
}
```
It will return a JSON of the following type:
```
{
  chat_id: ... (str)
  prediction: ... (categorical)
  probability: ... float (0-1)
}
```
