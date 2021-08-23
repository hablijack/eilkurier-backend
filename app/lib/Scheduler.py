#!/usr/bin/python3
# -*- coding: utf-8 -*-

from apscheduler.schedulers.background import BackgroundScheduler
import feedparser
import logging
from lib.Persistence import Persistence


class Scheduler():

    def __init__(self):
        self.logger = logging.getLogger("Scheduler")
        self.logger.info("Initializing Scheduler ...")
        self.scheduler = BackgroundScheduler()
        self.register_jobs()

    def register_jobs(self):
        self.logger.info("Registering Jobs ...")
        feeds = [
            {'identifier': 'Golem.de News', 'description': 'IT-News fuer Profis',
                'url': 'https://rss.golem.de/rss.php?feed=ATOM2.0', 'icon': 'https://www.golem.de/staticrl/images/golem-rss.png'},
            {'identifier': 'heise online News', 'description': 'Nachrichten nicht nur aus der Welt der Computer',
                'url': 'https://www.heise.de/newsticker/heise-atom.xml', 'icon': 'https://1.f.ix.de/icons/ho/heise_online_min.gif'},
            {'identifier': 'Adafruit Learning', 'description': 'Adafruit Learning System', 'url': 'http://feeds.feedburner.com/d0od?format=xml',
                'icon': 'https://d2794n4cyhr13z.cloudfront.net/assets/c164befb381ecf49c07e8b058f6894e6.png'},
            {'identifier': 'OMG! Ubuntu!', 'description': 'Ubuntu News', 'url': 'http://feeds.feedburner.com/d0od?format=xml',
                'icon': 'http://www.omgubuntu.co.uk/wp-content/themes/omgubuntu-theme-3.6.1/images/logo.png'},
            {'identifier': 'Winfuture', 'description': 'Winfuture News',
                'url': 'http://static.winfuture.de/feeds/WinFuture-News-rss2.0.xml', 'icon': 'https://i.wfcdn.de/5/header_left.jpg'},
            {'identifier': 'ZDNet', 'description': 'IT-News und Hintergrundartikel für IT-Manager', 'url': 'http://www.zdnet.de/feed/',
                'icon': 'http://www.zdnet.de/wp-content/themes/korasa-zdnet-de/assets/images/logos/zdnet-de-gray-drp.png'},
            {'identifier': 't3n digital pioneers', 'description': 't3n digital pioneers - News', 'url': 'https://t3n.de/rss.xml',
                'icon': 'https://t3n.de/_Resources/Static/Packages/Yeebase.t3nDe/Images/Rss/logo.png'},
            {'identifier': 'Hackaday', 'description': 'Fresh hacks every day', 'url': 'https://hackaday.com/feed/',
                'icon': 'https://secure.gravatar.com/blavatar/5560f98f805877b0e332f191cb9e0af3?s=96&d=https%3A%2F%2Fs2.wp.com%2Fi%2Fbuttonw-com.png'},
            {'identifier': 'Lifehacker', 'description': 'Tips, tricks and downloads for getting things done',
                'url': 'https://lifehacker.com/rss', 'icon': 'https://t3n.de/_Resources/Static/Packages/Yeebase.t3nDe/Images/Rss/logo.png'},
            {'identifier': 'Heise Make', 'description': 'Kreativ mit Technik',
                'url': 'https://www.heise.de/make/rss/hardware-hacks-atom.xml', 'icon': 'https://1.f.ix.de/make/icons/make_logo.png'},
            {'identifier': 'GITARRE & BASS', 'description': 'Das Musiker-Fachmagazin', 'url': 'https://www.heise.de/make/rss/hardware-hacks-atom.xml',
                'icon': 'https://www.gitarrebass.de/wp-content/uploads/2015/04/GITARRE-BASS_Logo.png'},
            {'identifier': 'Guitar', 'description': 'Magazin für Gitarristen und Bassisten',
                'url': 'https://guitar.de/?format=feed&type=rss', 'icon': 'https://www.guitar.de/fileadmin/templates/img/guitar_logo_500.png'},
            {'identifier': 'Keys', 'description': 'Musik und Computer', 'url': 'https://www.keys.de/?format=feed&type=rss',
                'icon': 'https://www.keys.de/fileadmin/templates/keys/img/KEYS_Logo.png'},
            {'identifier': 'DrUmHeaDS', 'description': '100% Schlagzeug', 'url': 'https://www.drumheads.de/?id=39&format=feed&type=rss',
                'icon': 'https://www.drumheads.de/fileadmin/templates/img/DH_Logo_600.png'},
            {'identifier': 'SOUNDCHECK', 'description': 'Das Bandmagazin', 'url': 'https://www.soundcheck.de/index.php?id=38',
                'icon': 'https://www.soundcheck.de/fileadmin/templates/img/SC_Logo_290.png'},
            {'identifier': 'Digital Present', 'description': 'Tagesspiegel', 'url': 'http://digitalpresent.tagesspiegel.de/feed.xml',
                'icon': 'http://digitalpresent.tagesspiegel.de/images/tagesspiegel.png'},
            {'identifier': 'Welt', 'description': 'Nachrichten - Aktuelle Nachrichten von WELT',
                'url': 'https://www.welt.de/feeds/latest.rss', 'icon': 'https://www.welt.de/rss-logo.png'},
            {'identifier': 'Wirtschaftsmagazin', 'description': 'Das Online-Portal des führenden Wirtschaftsmagazins in Deutschland. Das Entscheidende zu Unternehmen, Finanzen,Erfolg und Technik.',
                'url': 'http://www.wiwo.de/contentexport/feed/rss/schlagzeilen', 'icon': 'http://www.wiwo.de/images/wiwo-logo-main/10269318/5-formatOriginal.png'},
            {'identifier': 'ZEIT ONLINE', 'description': 'Nachrichten, Hintergründe und Debatten',
                'url': 'https://newsfeed.zeit.de/index', 'icon': 'http://img.zeit.de/bilder/elemente_01_06/logos/homepage_top.gif'},
            {'identifier': 'SPIEGEL ONLINE', 'description': 'Deutschlands führende Nachrichtenseite. Alles Wichtige aus Politik, Wirtschaft, Sport, Kultur, Wissenschaft, Technik und mehr.',
                'url': 'https://updated.de/feed/rss', 'icon': 'https://www.otto.de/unternehmen/media-oc/img/newsroom/bilder/pressedownload/2017/weblication/wThumbnails/UPDATED_LOGO-e1fd9b6770d725bg274ffe8fbc40d0f3.JPG'},
            {'identifier': 'Otto.de Updated', 'description': 'Otto.de Tech Blog',
                'url': 'http://www.spiegel.de/index.rss', 'icon': 'http://www.spiegel.de/static/sys/logo_120x61.gif'},
            {'identifier': 'taz.de', 'description': 'Aktuelle Nachrichten - Netzkultur', 'url': 'http://www.taz.de/!p4631;rss/',
                'icon': 'http://www.taz.de/fileadmin/templates/neu/images/logo_tazde_rss_2.gif'},
            {'identifier': 'scilogs.spektrum.de', 'description': 'Tagebücher der Wissenschaft', 'url': 'https://scilogs.spektrum.de/feed/',
                'icon': 'https://scilogs.spektrum.de/wp-content/themes/sdw-theme/assets/img/favicon/scilogs-icon-32.png'},
            {'identifier': 'Motherboard', 'description': 'The future is wonderful, the future is terrifying',
                'url': 'https://motherboard.vice.com/de/rss', 'icon': 'https://vice-web-statics-cdn.vice.com/images/motherboard-og.png'},
            {'identifier': 'China-Gadgets', 'description': 'Die besten Gadgets aus China!', 'url': 'https://www.china-gadgets.de/feed/',
                'icon': 'https://www.china-gadgets.de/app/plugins/tmx-navigation-menu/assets/logo.svgg'},
            {'identifier': 'Jetzt.de', 'description': 'Was junge Menschen jetzt bewegt: Aktuelles mit Tiefe - aus Gesellschaft, Liebe und Sex, Studium und Job, Politik und Popkultur, Musik, TV und Kino.',
                'url': 'https://www.jetzt.de/alle_artikel.rss', 'icon': 'https://www.jetzt.de/images/ms-icon-310x310.png'},
        ]
        for feed in feeds:
            self.scheduler.add_job(
                name=feed['identifier'],
                id=feed['identifier'],
                func=self.fetch_feed,
                args=[feed],
                trigger='interval',
                seconds=60
            )

    def fetch_feed(self, feed):
        logger = logging.getLogger("FeedFetcher")
        logger.info("parsing RSS recipe of the day")
        try:
            persistence = Persistence()
            persistence.connect()
            cursor = persistence.connection.cursor()
            response = feedparser.parse(feed['url'])
            for entry in response.entries:
                persistence.insert_info(
                    cursor,
                    feed['id'],
                    entry['date'],
                    entry['title'],
                    entry['author'],
                    entry['link'],
                    entry['picture'],
                    entry['message'],
                    entry['textonly_message']
                )
            persistence.connection.commit()
            cursor.close()
            persistence.disconnect()
        except Exception as e:
            logger.error(feed['identifier'])
            logger.error("Error: %s. Cannot get Feed." % e)

    def lookup_job(self, identifier):
        found = None
        for job in self.scheduler.get_jobs():
            if job.id == identifier:
                found = job
                break
        return found

    def remove_job(self, identifier):
        self.scheduler.remove_job(identifier)

    def start(self):
        self.scheduler.start()
