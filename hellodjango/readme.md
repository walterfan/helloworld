# python
sudo apt install python3-pip
pip3 install --upgrade pip
pip3 install virtualenv
virtualenv --python=python3 ../venv
source ../venv/bin/activate

# django
pip install django==2.0.5
django-admin.py startproject pims
python3 manage.py startapp checklist

python3 manage.py collectstatic --noinput

# Nginx
sudo apt install nginx
sudo service nginx start

* nginx.conf.j2

```
server {
	listen 80;
	servername www.192.168.3.8.xip.io;
	
	location /static {
		alias /opt/sites/pims/static
	}

	location / {
		proxy_pass:http://localhost:8000;
	}

}

```


# Gunicorn

pip install gunicorn
gunicorn pims.wsgi:application

# folder

/opt/sites/pims
  - database
  - source
  - static
  - venv

# Mezzanine

## Install from PyPI
$ pip install mezzanine

## Create a project
$ mezzanine-project mezzsite
$ cd mezzsite

## Create a database
$ python manage.py createdb

## Run the web server
$ python manage.py runserver



