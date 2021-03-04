#!/usr/bin/python3
# -*- coding: utf-8 -*-

import logging
from lib.Scheduler import Scheduler
from lib.Configuration import Configuration
from waitress import serve
from flask import Flask, request

app = Flask(__name__)
scheduler = Scheduler()

@app.route('/health')
def health():
    return {'status': 'UP'}


if __name__ == "__main__":
    logging.basicConfig(
        level=logging.INFO,
        format="%(asctime)s - %(levelname)s [%(name)s] %(message)s",
        datefmt='%d.%m.%Y %H:%M:%S',
        handlers=[
            logging.StreamHandler()
        ]
    )
    scheduler.start()
    serve(app, port=Configuration().app_port())
