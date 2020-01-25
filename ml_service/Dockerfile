FROM python:3.6.9
WORKDIR /code
RUN pip install --upgrade pip
ENV FLASK_APP ml_app.py
ENV FLASK_RUN_HOST 0.0.0.0
ENV FLASK_RUN_PORT=5001
COPY requirements.txt requirements.txt
RUN pip install -r requirements.txt
COPY . .
CMD ["flask", "run"]