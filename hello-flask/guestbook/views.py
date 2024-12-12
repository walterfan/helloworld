from flask import Flask
from flask import request
from flask import render_template
from flask import redirect
from flask import url_for

import markdown

from guestbook import app, db, logger
from guestbook.models import Message
from guestbook.forms import MessageForm

@app.route("/")
def main():
    return render_template('index.html')


@app.route("/guestbook")
def guestbook():
    messages = Message.query.order_by(Message.createTime.desc()).all()
    form = MessageForm()
    results = []
    return render_template('guestbook.html', results=results)


@app.route("/help")
def readme():
    with open("../readme.md") as fp:
        fileContent = fp.read()
        html = markdown.markdown(fileContent, extensions=['markdown.extensions.tables', 'markdown.extensions.nl2br'])
        return html


@app.route("/clear")
def clear():
    #table.delete()
    results = []
    return render_template('guestbook.html', results=results)


@app.route("/addmessage", methods=['POST'])
def submit():
    parameters = dict(command=request.form['subject'], input=request.form['content'])
    output = parameters["input"]
    logger.info(parameters)
    parameters["author"] = request.form['author']
    #table.insert(parameters)
    return redirect(url_for('guestbook'))


if __name__ == "__main__":
    app.run(port=5000, debug=True)
