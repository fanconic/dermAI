FROM ubuntu:18.04
RUN apt-get update -y
RUN apt-get install -y python-pip python-dev build-essential
COPY ./backend /backend
COPY requirements.txt /backend
WORKDIR /backend
RUN pip install -r requirements.txt
ENTRYPOINT ["python"]
CMD ["backend_db.py"]