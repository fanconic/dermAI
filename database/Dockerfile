FROM python:3.7-alpine
WORKDIR /code
RUN pip install --upgrade pip
RUN apk add --no-cache gcc musl-dev linux-headers
COPY requirements.txt requirements.txt
RUN pip install -r requirements.txt
COPY . .
CMD ["python", "db_app.py"]