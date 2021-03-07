#!/usr/bin/python3
# -*- coding: utf-8 -*-

import psycopg2
import logging
from lib.Configuration import Configuration

""""
Persistence
"""


class Persistence:

    def __init__(self):
        config = Configuration()
        self.HOST = config.db_host()
        self.USER = config.db_user()
        self.PASSWORD = config.db_password()
        self.DATABASE = config.db_name()

    def connect(self):
        try:
            self.connection = psycopg2.connect(
                database=self.DATABASE,
                user=self.USER,
                password=self.PASSWORD,
                host=self.HOST,
                port=5432
            )
            return True
        except Exception as e:
            logging.error("Error on opening database connection")
            logging.error(e)
            return False

    def disconnect(self):
        try:
            self.connection.close()
        except Exception as e:
            logging.error("Error on disconnecting from database")
            logging.error(e)
