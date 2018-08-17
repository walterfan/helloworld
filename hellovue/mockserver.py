import os
import json
import requests
from flask_httpauth import HTTPBasicAuth
from flask import Flask, request, redirect, make_response, url_for, send_from_directory, Response
from werkzeug.exceptions import NotFound, ServiceUnavailable


MOCK_API_PATH = "/api/v1"
DEFAULT_PORT = 8021

app = Flask(__name__)

current_path = os.path.dirname(os.path.realpath(__file__))


MOCK_JSON_FILE_MSG = "{}/tasks.json".format(current_path)


def read_data(json_file):
    json_fp = open(json_file, "r")
    return json.load(json_fp)


def generate_response(arg, contentType="application/json" , response_code=200):
    response = make_response(json.dumps(arg, sort_keys = True, indent=4))
    response.headers['Content-type'] = contentType
    response.headers['ETag'] = "%s" % id(arg)
    response.status_code = response_code
    return response

@app.route('/')
def root():
    return send_from_directory('.','index.html')

@app.route('/js/<path:path>')
def send_js(path):
    return send_from_directory('js', path)

@app.route('/css/<path:path>')
def send_css(path):
    return send_from_directory('css', path)

@app.route('/tasks', methods=['GET'])
def tasks():
    jsonNode = read_data(MOCK_JSON_FILE_MSG)
    return generate_response(jsonNode)

@app.route('/tasks', methods=['POST'])
def addtask():
    jsonNode = read_data(MOCK_JSON_FILE_MSG)
    return generate_response(jsonNode)    

@app.route('/<path:path>')
def static_proxy(path):
  # send_static_file will guess the correct MIME type
  return send_from_directory('.', path)

if __name__ == "__main__":
    app.run(port=DEFAULT_PORT, debug=True)
