from flask import Flask
from flask import request
from flask import render_template
from flask import redirect
from flask import url_for
import time 
import account
import dataset
import markdown


import os
import sys
import logging

def init_logger(filename):
    logger = logging.getLogger(filename)
    logger.setLevel(logging.INFO)

    formatstr = '%(asctime)s - [%(filename)s:%(lineno)d] - %(levelname)s - %(message)s'
    consoleHandler = logging.StreamHandler(sys.stdout)
    consoleHandler.setFormatter(logging.Formatter(formatstr))
    logger.addHandler(consoleHandler)

    logfile = os.path.join(".", filename + '.log')
    fileHandler = logging.FileHandler(logfile)
    logger.addHandler(fileHandler)
    return logger

logger = init_logger(__name__)


# Connnect to database
db = dataset.connect('sqlite:///guestbook.db')

# create your local table
table = db['results']

app = Flask(__name__)
	
@app.route("/")
def main():
   return render_template('index.html')

@app.route("/guestbook")
def guestbook():
    results = table.find()
    return render_template('guestbook.html', results=results)

@app.route("/help")
def readme():
    with open("readme.md") as fp:
        fileContent = fp.read()
        html = markdown.markdown(fileContent, extensions=['markdown.extensions.tables', 'markdown.extensions.nl2br'])
        return html


@app.route("/clear")
def clear():
    table.delete()
    results = []
    return render_template('guestbook.html', results=results)


@app.route("/addmessage", methods=['POST'])
def submit():
    parameters = dict(command=request.form['subject'], input=request.form['content'])
    output = parameters["input"]
    logger.info(parameters)
    parameters["author"] = request.form['author']
    table.insert(parameters)
    return redirect(url_for('guestbook'))

if __name__ == "__main__":

	app.run(port=5000, debug=True)

