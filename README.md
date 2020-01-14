# DermAItoscope

This is the repository for the DermAItoscope, a skin cancer classifying app, which runs on a Flask backend.

The model is trained on a EfficientNet and achieves approximately 92% of AUC on a balanced dataset.
Further infomration about the computer vision model can be found here: https://www.kaggle.com/fanconic/efficientnetb3-approach-test-auc-0-920

All the backend can be started via a docker container with the following commands:
Build docker image:
```
$ docker build -t dermai-backend:latest .
```

Run docker image 
```
$ docker run -d -p 5000:5000 dermai-backend
```

![DermAI Demo](dermai_gif.gif)
