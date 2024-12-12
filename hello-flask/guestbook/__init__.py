from flask import Flask
from flask_sqlalchemy import SQLAlchemy
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

    logfile = os.path.join("..", filename + '.log')
    fileHandler = logging.FileHandler(logfile)
    logger.addHandler(fileHandler)
    return logger


logger = init_logger(__name__)
basedir = os.path.abspath(os.path.dirname(__file__))

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///' + os.path.join(basedir, 'guestbook.db')
app.config['SQLALCHEMY_COMMIT_ON_TEARDOWN'] = True
db = SQLAlchemy(app)