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
db = dataset.connect('sqlite:///ivr-service.db')

# create your local table
table = db['results']

app = Flask(__name__)
	
@app.route("/")
def main():
   return render_template('index.html')

@app.route("/console")
def web_console():
    results = table.find()
    return render_template('web_console.html', results=results)

@app.route("/help")
def readme():
    with open("README.md") as fp:
        fileContent = fp.read()
        html = markdown.markdown(fileContent, extensions=['markdown.extensions.tables', 'markdown.extensions.nl2br'])
        return html


@app.route("/console/clear")
def clear_console():
    table.delete()
    results = []
    return render_template('web_console.html', results=results)


@app.route("/console/execute", methods=['POST'])
def submit():
    parameters = dict(command=request.form['command'], input=request.form['input'])
    output = parameters["input"]
    logger.info(parameters)
    parameters["output"] = output
    table.insert(parameters)
    return redirect(url_for('web_console'))

if __name__ == "__main__":

	app.run(port=5000, debug=True)

