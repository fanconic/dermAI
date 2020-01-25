FROM python:3.6.9
WORKDIR /code
RUN pip install --upgrade pip
COPY requirements.txt requirements.txt
RUN pip install -r requirements.txt
COPY . .
CMD ["python", "ml_app.py"]