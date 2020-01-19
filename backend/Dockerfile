FROM ubuntu:18.04
COPY ./ /backend
COPY requirements.txt /backend
WORKDIR /backend
RUN pip install -r requirements.txt
ENTRYPOINT ["python"]
CMD ["backend_db.py"]