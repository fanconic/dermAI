FROM python:3.7-alpine
WORKDIR /code
RUN pip install --upgrade pip
ENV FLASK_APP db_app.py
ENV FLASK_RUN_HOST 0.0.0.0
ENV FLASK_RUN_PORT=5000
RUN apk add --no-cache gcc musl-dev linux-headers
COPY requirements.txt requirements.txt
RUN pip install -r requirements.txt
COPY . .
CMD ["flask", "run"]