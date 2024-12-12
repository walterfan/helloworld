from guestbook import db
from datetime import datetime

class Message(db.Model):
    __tablename__ = 'guestbook'
    id = db.Column(db.Integer, primary_key=True)
    subject = db.Column(db.String(64), index=True)
    content = db.Column(db.Text(4096))
    tags = db.Column(db.String(128))
    author = db.Column(db.String(32))
    createTime = db.Column(db.DateTime, default=datetime.now, index=True)
