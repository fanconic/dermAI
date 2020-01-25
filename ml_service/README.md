# Machine Learning Service
This is the backend for the machine learning service.
The classifier was separately trained on kaggle at https://www.kaggle.com/fanconic/skin-cancer-efficientnetb0.
Transfer learning is used on an EfficientNetB0, which is a recently released CNN by Google, whihc achieves high results (AUC 0.92) with only few parameters (~4 mio.).