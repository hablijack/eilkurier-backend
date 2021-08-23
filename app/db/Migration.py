#!/usr/bin/python3
# -*- coding: utf-8 -*-

import logging
from lib.Configuration import Configuration
from lib.Persistence import Persistence


class Migration:
    def __init__(self):
        self.logger = logging.getLogger("Migration")
        self.logger.info("Initializing Migration ...")
        config = Configuration()
        self.persistence = Persistence()

    def migrate(self):
        self.logger.info("Starting to migrate database ...")
        if self.persistence.connect():
            self.logger.info("Connection is active, do some work ...")
            self.generate_tablestructure()
            self.insert_categories()
            self.persistence.disconnect()
            self.logger.info("Migration done!")
        else:
            self.logger.error(
                "No migration possible due to missing database connection!")

    def generate_tablestructure(self):
        try:
            self.logger.info("Generating table structure ...")
            self.cursor = self.persistence.connection.cursor()
            self.logger.info("Creating category table ...")
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
            self.logger.info("Creating feed table ...")
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
            self.logger.info("Creating info table ...")
            self.cursor.execute(
                """
                CREATE TABLE IF NOT EXISTS info 
                ( info_id INT GENERATED ALWAYS AS IDENTITY,
                  feed_id INT,
                  timestamp TIMESTAMP,
                  title VARCHAR(255) UNIQUE NOT NULL,
                  author VARCHAR(255),
                  link VARCHAR(255),
                  picture VARCHAR(255),
                  message TEXT,
                  textonly_message TEXT,
                  PRIMARY KEY(info_id),
                  CONSTRAINT fk_feed
                    FOREIGN KEY(feed_id) 
                );
                """
            )
            self.logger.info("Creating eilkurieruser table ...")
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
            self.logger.info("Creating eilkurieruser_feed table ...")
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
            self.logger.info("Commiting Database session ...")
            self.persistence.connection.commit()
            self.logger.info("Closing cursor ...")
            self.cursor.close()
            self.logger.info("Table structure successfully created!")
        except Exception as e:
            self.logger.error("Error on migrating database")
            self.logger.error(e)

    def insert_category_if_not_exists(self, name, description):
        self.logger.info("Trying to findout if category exists: %s", (name))
        self.cursor.execute(
            """SELECT * FROM category WHERE name like '%s'""" % name
        )
        records = self.cursor.fetchall()
        if len(records) <= 0:
            self.logger.info("Category does not exist! Insert it!")
            self.cursor.execute(
                """INSERT INTO category(name, description) VALUES ('%s', '%s');""" %
                (name, description)
            )
        else:
            self.logger.info("Category %s already exists", name)

    def insert_categories(self):
        try:
            self.logger.info("Inserting Categories ...")
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
            self.logger.info("Commiting database session ...")
            self.persistence.connection.commit()
            self.logger.info("Closing cursor ...")
            self.cursor.close()
        except Exception as e:
            self.logger.error("Error on migrating database")
            self.logger.error(e)
