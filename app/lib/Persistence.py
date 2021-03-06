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

    def migrate(self):
        try:
            cursor = self.connection.cursor()
            cursor.execute(
                """
                CREATE TABLE feed IF NOT EXISTS
                ( id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name VARCHAR(50),
                    description VARCHAR(100),
                    picture VARCHAR(200),
                    url VARCHAR(200)
                );
                """
            )
            cursor.execute(
                """
                CREATE TABLE user IF NOT EXISTS
                (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    email VARCHAR(100),
                    password_hash VARCHAR(100),
                    role VARCHAR(50),
                    wizzard_run_flag BOOLEAN NOT NULL
                );
                """
            )
            cursor.execute(
                """
                CREATE TABLE eilkurier_user_feeds IF NOT EXISTS
                (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    eilkurier_user_id INTEGER,
                    feed_id INTEGER,
                    sortindex INTEGER
                );
                """
            )
            cursor.execute(
                """
                CREATE TABLE category IF NOT EXISTS
                (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name  VARCHAR(100),
                    description TEXT
                );
                """
            )
            cursor.execute(
                """
                CREATE TABLE category_feed IF NOT EXISTS
                (
                    category_id INTEGER,
                    feed_id INTEGER
                );
                """
            )

            self.connection.commit()
            cursor.close()
        except Exception as e:
            print("Error on migrating database")
            print(e)
