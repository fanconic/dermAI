# DermAItoscope
## Introduction

This is the repository for the DermAItoscope, a skin cancer classifying app, which runs on a Flask backend.

The model is trained on a EfficientNet and achieves approximately 92% of AUC on a balanced dataset.
Further infomration about the computer vision model can be found here: https://www.kaggle.com/fanconic/skin-cancer-efficientnetb0


## Replicate Backend

If you wish to replicate the backend service, you can clone this repository.
```
$ git clone https://github.com/fanconic/dermAI
```

Move into the source directory:
```
$ cd dermAI
```

The backend microservices can be started with the following command:
```
$ sudo docker-compose up
```

## Demo GIF of DermAI 2.0 
Currently working on version 2.0\
![DermAI Demo](dermai_gif.gif)
