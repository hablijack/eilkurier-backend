#!/usr/bin/python3
# -*- coding: utf-8 -*-

import logging
from lib.Configuration import Configuration
from lib.Persistence import Persistence


class Migration:
    def __init__(self):
        config = Configuration()
        self.persistence = Persistence()

    def migrate(self):
        if self.persistence.connect():
            self.generate_tablestructure()
            self.insert_categories()
            self.persistence.disconnect()
        else:
            logging.error("No connection to database possible!")

    def generate_tablestructure(self):
        try:
            self.cursor = self.persistence.connection.cursor()
            self.cursor.execute(
                """
                CREATE TABLE IF NOT EXISTS category 
                (
                    category_id INT GENERATED ALWAYS AS IDENTITY,
                    name VARCHAR(255) UNIQUE NOT NULL,
                    description TEXT NOT NULL,
                    PRIMARY KEY(category_id)
                );
                """
            )
            self.cursor.execute(
                """
                CREATE TABLE IF NOT EXISTS feed 
                ( feed_id INT GENERATED ALWAYS AS IDENTITY,
                  category_id INT,
                  name VARCHAR(255) UNIQUE NOT NULL,
                  description VARCHAR(255),
                  picture VARCHAR(255),
                  url VARCHAR(255) UNIQUE NOT NULL,
                  PRIMARY KEY(feed_id),
                  CONSTRAINT fk_category
                    FOREIGN KEY(category_id) 
	                REFERENCES category(category_id)
                    ON DELETE SET NULL
                );
                """
            )
            self.cursor.execute(
                """
                CREATE TABLE IF NOT EXISTS eilkurieruser 
                (
                    user_id INT GENERATED ALWAYS AS IDENTITY,
                    email VARCHAR(255),
                    password_hash VARCHAR(255),
                    role VARCHAR(255),
                    wizzard_run_flag BOOLEAN NOT NULL DEFAULT false,
                    PRIMARY KEY(user_id)
                );
                """
            )
            self.cursor.execute(
                """
                CREATE TABLE IF NOT EXISTS eilkurieruser_feed 
                (
                    user_id INT REFERENCES eilkurieruser(user_id) ON DELETE CASCADE,
                    feed_id INT REFERENCES feed(feed_id) ON DELETE CASCADE,
                    sortindex INTEGER,
                    CONSTRAINT user_feed_pkey 
                        PRIMARY KEY (user_id, feed_id)
                );
                """
            )
            self.persistence.connection.commit()
            self.cursor.close()
        except Exception as e:
            logging.error("Error on migrating database")
            logging.error(e)

    def insert_category_if_not_exists(self, name, description):
        self.cursor.execute(
            """SELECT * FROM category WHERE name like '%s'""" % name
        )
        records = self.cursor.fetchall()
        if len(records) <= 0:
            self.cursor.execute(
                """INSERT INTO category(name, description) VALUES ('%s', '%s');""" %
                (name, description)
            )
        else:
            logging.info("Category %s already exists", name)

    def insert_categories(self):
        try:
            self.cursor = self.persistence.connection.cursor()
            self.insert_category_if_not_exists(
                'IT, Hardware, Software',
                'Nachrichten- und Artikelquellen über die digitale Welt.'
            )
            self.insert_category_if_not_exists(
                'DIY, Maker, Heimwerken, Basteln',
                'Anleitungen und Beschreibungen von selbstgebauten Projekten.'
            )
            self.insert_category_if_not_exists(
                'Musik, Band, Instrumente',
                'Nachrichten- und Artikelquellen über das Musizieren.'
            )
            self.insert_category_if_not_exists(
                'Gitarre & Bass',
                'Nachrichten- und Artikelquellen für Gitarristen und Bassisten.'
            )
            self.insert_category_if_not_exists(
                'Keyboard',
                'Nachrichten- und Artikelquellen für Keyboarder.'
            )
            self.insert_category_if_not_exists(
                'Schlagzeug',
                'Nachrichten- und Artikelquellen für Schlagzeuger'
            )
            self.insert_category_if_not_exists(
                'Nachrichten',
                'Schlagzeilen und Neuigkeiten.'
            )
            self.insert_category_if_not_exists(
                'Gadgets',
                'Tools & Gadgets'
            )
            self.insert_category_if_not_exists(
                'Jugend',
                'Was junge Menschen jetzt bewegt.'
            )
            self.persistence.connection.commit()
            self.cursor.close()
        except Exception as e:
            logging.error("Error on migrating database")
            logging.error(e)
