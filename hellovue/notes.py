import os
import json
import requests
from flask_httpauth import HTTPBasicAuth
from flask import make_response
from flask import Flask
from flask import request
from werkzeug.exceptions import NotFound, ServiceUnavailable

NOTES_API_PATH = "/api/v1/notes"

app = Flask(__name__)

current_path = os.path.dirname(os.path.realpath(__file__))

auth = HTTPBasicAuth()

users = {
    "walter": "pass1234"
}

json_file = "{}/note.json".format(current_path)

def read_data():
    json_fp = open(json_file, "r")
    return json.load(json_fp)

def save_data(notes):
    json_fp = open(json_file, "w")
    json.dump(notes, json_fp, sort_keys = True, indent = 4)

@auth.get_password
def get_pw(username):
    if username in users:
        return users.get(username)
    return None

def generate_response(arg, response_code=200):
    response = make_response(json.dumps(arg, sort_keys = True, indent=4))
    response.headers['Content-type'] = "application/json"
    response.status_code = response_code
    return response

@app.route(NOTES_API_PATH, methods=['GET'])
@auth.login_required
def index():
    return generate_response({
        "username": auth.username(),
        "description": "note micro service /notes"
    })

@auth.login_required
@app.route(NOTES_API_PATH, methods=['GET'])
def list_note():
    notes = read_data()
    return generate_response(notes)

#Create note
@auth.login_required
@app.route(NOTES_API_PATH, methods=['POST'])
def create_note():
    note = request.json
    noteId = note["id"]
    notes = read_data()
    if noteId in notes:
        return generate_response({"error": "conflict"}, 409)
    notes[noteId] = note
    save_data(notes)
    return generate_response(note)

#Retrieve note
@auth.login_required
@app.route(NOTES_API_PATH + '/<noteId>', methods=['GET'])
def retrieve_note(noteId):
    notes = read_data()
    if noteId not in notes:
        return generate_response({"error": "not found"}, 404)

    return generate_response(notes[noteId])

#Update note
@auth.login_required
@app.route(NOTES_API_PATH + '/<noteId>', methods=['PUT'])
def update_note(noteId):
    notes = read_data()
    if noteId not in notes:
        return generate_response({"error": "not found"}, 404)

    note = request.json
    print(note)
    notes[noteId] = note
    save_data(notes)
    return generate_response(note)

#Delete note
@auth.login_required
@app.route(NOTES_API_PATH + '/<noteId>', methods=['DELETE'])
def delete_note(noteId):
    notes = read_data()
    if noteId not in notes:
        return generate_response({"error": "not found"}, 404)

    del(notes[noteId])
    save_data(notes)
    return generate_response("", 204)

if __name__ == "__main__":
    app.run(port=5000, debug=True)


