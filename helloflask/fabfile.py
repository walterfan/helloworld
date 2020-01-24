from fabric.api import local
from guestbook import db

deps = ['flask','flask-httpauth','requests','httpie']

def app_run():
	local("python account.py")

def install_deps():
	for dep in deps:
		local("sudo pip install %s" % dep)

def initdb():
	db.create_all()