#!/usr/bin/python3
# -*- coding: utf-8 -*-

import configparser
import os

""""
Represents the properties file and gives access to configuration options
"""


class Configuration:

    def __init__(self):
        self.config = configparser.ConfigParser()
        thisfolder = os.path.dirname(os.path.abspath(__file__))
        self.config.read(os.path.join(
            thisfolder, '../configuration.properties'))

    def app_port(self):
        return int(self.config.get('APPLICATION', 'port'))

    def db_host(self):
        return os.getenv('DB_HOST')

    def db_name(self):
        return os.getenv('DB_NAME')

    def db_user(self):
        return os.getenv('DB_USER')

    def db_password(self):
        return os.getenv('DB_PASSWORD')
