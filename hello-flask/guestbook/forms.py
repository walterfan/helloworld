from flask_wtf import FlaskForm

from wtforms import StringField, SubmitField, TextAreaField
from wtforms.validators import DataRequired, Length

class MessageForm(FlaskForm):
    subject = StringField('Subject', validators=[DataRequired(), Length(1, 32)])
    content = TextAreaField('Content', validators=[DataRequired(), Length(1 ,4096)])
    submit = SubmitField()