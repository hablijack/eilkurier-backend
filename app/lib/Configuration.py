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
        self.config.read(os.path.join(thisfolder, '../configuration.properties'))

    def app_port(self):
        return int(self.config.get('APPLICATION', 'port'))
