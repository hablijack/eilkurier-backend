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
        self.logger = logging.getLogger("Persistence")
        self.logger.info("Initializing Persistence ...")
        config = Configuration()
        self.HOST = config.db_host()
        self.USER = config.db_user()
        self.PASSWORD = config.db_password()
        self.DATABASE = config.db_name()

    def insert_info(self, cursor, feed_id, timestamp, title, author, link, picture, message, textonly_message):
        cursor.execute(
            """INSERT INTO info
               (feed_id, timestamp, title, author, link, picture, message, textonly_message) 
               VALUES ('%s', '%s');
            """ % (feed_id, timestamp, title, author, link, picture, message, textonly_message)
        )

    def connect(self):
        try:
            self.logger.info("Establishing database connection ...")
            self.connection = psycopg2.connect(
                database=self.DATABASE,
                user=self.USER,
                password=self.PASSWORD,
                host=self.HOST,
                port=5432
            )
            self.logger.info("Successfully connected to database!")
            return True
        except Exception as e:
            self.logger.error("Error on opening database connection")
            self.logger.error(e)
            return False

    def disconnect(self):
        try:
            self.logger.info("Closing database connection ...")
            self.connection.close()
            self.logger.info("Database connected closed successfully!")
        except Exception as e:
            self.logger.error("Error on disconnecting from database")
            self.logger.error(e)
