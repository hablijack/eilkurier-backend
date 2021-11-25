package de.hablijack.eilkurier.lifecycle;

import de.hablijack.eilkurier.entity.Category;
import de.hablijack.eilkurier.entity.Feed;
import de.hablijack.eilkurier.service.FeedService;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import java.io.IOException;
import java.text.ParseException;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;


@Startup
@ApplicationScoped
public class DatabaseInitialLoad {

  @Inject
  FeedService feedService;

  @Transactional
  @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:LineLength"})
  public void initializeWithBaseData(@Observes StartupEvent event)
      throws XMLStreamException, IOException, ParserConfigurationException, ParseException, SAXException {
    Category it = new Category("IT, Hardware, Software", "Nachrichten- und Artikelquellen über die digitale Welt.");
    it.persistIfNotExist();
    new Feed("Golem.de News", "IT-News fuer Profis", "https://www.golem.de/staticrl/images/golem-rss.png", "image/png",
        "https://rss.golem.de/rss.php?feed=ATOM2.0", it).persistIfNotExist();
    new Feed("heise online News", "Nachrichten nicht nur aus der Welt der Computer",
        "https://1.f.ix.de/icons/ho/heise_online_min.gif", "image/gif",
        "https://www.heise.de/newsticker/heise-atom.xml", it).persistIfNotExist();
    new Feed("OMG! Ubuntu!", "Ubuntu News",
        "http://www.omgubuntu.co.uk/wp-content/themes/omgubuntu-theme-3.6.1/images/logo.png", "image/png",
        "http://feeds.feedburner.com/d0od?format=xml", it).persistIfNotExist();
    new Feed("Winfuture", "Winfuture News", "https://i.wfcdn.de/5/header_left.jpg", "image/jpg",
        "https://static.winfuture.de/feeds/WinFuture-News-rss2.0.xml", it).persistIfNotExist();
    new Feed("ZDNet", "IT-News und Hintergrundartikel für IT-Manager",
        "http://www.zdnet.de/wp-content/themes/korasa-zdnet-de/assets/images/logos/zdnet-de-gray-drp.png", "image/png",
        "https://www.zdnet.de/feed/", it).persistIfNotExist();
    new Feed("t3n digital pioneers", "t3n digital pioneers - News",
        "https://t3n.de/_Resources/Static/Packages/Yeebase.t3nDe/Images/Rss/logo.png", "image/png",
        "https://t3n.de/rss.xml", it).persistIfNotExist();
    new Feed("Engadget", "Gadgets und elektronische Geräte",
        "http://de.engadget.com/assets/images/Engadget_DE_Black.png", "image/png", "https://www.engadget.com/rss.xml",
        it).persistIfNotExist();
    new Feed("Adafruit Blog", "electronics, open source hardware, hacking and more...",
        "https://cdn-blog.adafruit.com/uploads/2020/04/logo_small@2x.png", "image/png",
        "https://blog.adafruit.com/feed/", it).persistIfNotExist();
    // #############################################################################################################
    Category diy =
        new Category("DIY, Maker, Heimwerken, Basteln", "Anleitungen und Beschreibungen von selbstgebauten Projekten.");
    diy.persistIfNotExist();
    new Feed("Lifehacker", "Tips, tricks and downloads for getting things done",
        "https://t3n.de/_Resources/Static/Packages/Yeebase.t3nDe/Images/Rss/logo.png", "image/png",
        "https://lifehacker.com/rss", diy).persistIfNotExist();
    new Feed("Heise Make", "Kreativ mit Technik", "https://1.f.ix.de/make/icons/make_logo.png", "image/png",
        "https://www.heise.de/make/rss/hardware-hacks-atom.xml", diy).persistIfNotExist();
    new Feed("Hackaday", "Fresh hacks every day",
        "https://secure.gravatar.com/blavatar/5560f98f805877b0e332f191cb9e0af3?s=96&d=https%3A%2F%2Fs2.wp.com%2Fi%2Fbuttonw-com.png",
        "image/png", "https://hackaday.com/feed/", diy).persistIfNotExist();
    new Feed("Adafruit Learning", "Adafruit Learning System | Latest Published Guides",
        "https://d2794n4cyhr13z.cloudfront.net/assets/c164befb381ecf49c07e8b058f6894e6.png", "image/png",
        "https://learn.adafruit.com/feed", diy).persistIfNotExist();
    // #############################################################################################################
    Category music = new Category("Musik, Band, Instrumente", "Nachrichten- und Artikelquellen über das Musizieren.");
    music.persistIfNotExist();
    new Feed("GITARRE & BASS", "Das Musiker-Fachmagazin",
        "https://www.gitarrebass.de/wp-content/uploads/2015/04/GITARRE-BASS_Logo.png", "image/png",
        "https://www.gitarrebass.de/feed/", music).persistIfNotExist();
    new Feed("Guitar", "Magazin für Gitarristen und Bassisten",
        "https://www.guitar.de/fileadmin/templates/img/guitar_logo_500.png", "image/png",
        "https://music-product-news.de/gitarre-und-bass?format=feed&type=rss", music).persistIfNotExist();
    new Feed("Keys", "Musik und Computer", "https://www.keys.de/fileadmin/templates/keys/img/KEYS_Logo.png",
        "image/png", "https://www.keys.de/?format=feed&type=rss", music).persistIfNotExist();
    new Feed("SOUNDCHECK", "Das Bandmagazin", "https://www.soundcheck.de/fileadmin/templates/img/SC_Logo_290.png",
        "image/png", "https://www.soundcheck.de/?format=feed&type=rss", music).persistIfNotExist();
    // #############################################################################################################
    Category news = new Category("Nachrichten", "Schlagzeilen und Neuigkeiten.");
    news.persistIfNotExist();
    new Feed("Digital Present ", "Tagesspiegel", "http://digitalpresent.tagesspiegel.de/images/tagesspiegel.png",
        "image/png", "https://digitalpresent.tagesspiegel.de/feed.xml", news).persistIfNotExist();
    new Feed("ZEIT ONLINE", "Nachrichten, Hintergründe und Debatten",
        "http://img.zeit.de/bilder/elemente_01_06/logos/homepage_top.gif", "image/gif", "http://newsfeed.zeit.de/index",
        news).persistIfNotExist();
    new Feed("SPIEGEL ONLINE",
        "Deutschlands führende Nachrichtenseite. Alles Wichtige aus Politik, Wirtschaft, Sport, Kultur, Wissenschaft, Technik und mehr.",
        "http://www.spiegel.de/static/sys/logo_120x61.gif", "image/gif", "https://www.spiegel.de/index.rss",
        news).persistIfNotExist();
    new Feed("taz.de",
        "Kritischer, unabhängiger Journalismus der linken Nachrichtenseite taz: Analysen, Hintergründe, Kommentare, Interviews, Reportagen. Genossenschaft seit 1992.",
        "http://www.taz.de/fileadmin/templates/neu/images/logo_tazde_rss_2.gif", "image/gif",
        "https://taz.de/!p4608;rss/", news).persistIfNotExist();
    new Feed("Frankfurter Allgemeine Zeitung",
        "News, Nachrichten und aktuelle Meldungen aus allen Ressorts. Politik, Wirtschaft, Sport, Feuilleton und Finanzen im Überblick.",
        "https://media1.faz.net/ppmedia/aktuell/finanzen/4250185747/1.2649333/width610x580/f-a-z-logo.jpg", "image/gif",
        "https://www.faz.net/rss/aktuell/", news).persistIfNotExist();
    new Feed("Tagesschau",
        "die erste Adresse für Nachrichten und Information: An 365 Tagen im Jahr, rund um die Uhr aktualisiert - die wichtigsten News des Tages.",
        "https://www.tagesschau.de/tslogo-101~_v-videowebm.jpg", "image/jpg", "https://www.tagesschau.de/xml/rss2/",
        news).persistIfNotExist();
    new Feed("Sueddeutsche: Top-Themen",
        "die erste Adresse für Nachrichten und Information: An 365 Tagen im Jahr, rund um die Uhr aktualisiert - die wichtigsten News des Tages.",
        "https://id.sueddeutsche.de/img/sz_logo.png", "image/png", "https://rss.sueddeutsche.de/rss/Topthemen",
        news).persistIfNotExist();
    new Feed("Sueddeutsche: Alle-Themen",
        "die erste Adresse für Nachrichten und Information: An 365 Tagen im Jahr, rund um die Uhr aktualisiert - die wichtigsten News des Tages.",
        "https://id.sueddeutsche.de/img/sz_logo.png", "image/png",
        "https://rss.sueddeutsche.de/app/service/rss/alles/index.rss?output=rss", news).persistIfNotExist();
    new Feed("Sueddeutsche: Eilmeldungen",
        "die erste Adresse für Nachrichten und Information: An 365 Tagen im Jahr, rund um die Uhr aktualisiert - die wichtigsten News des Tages.",
        "https://id.sueddeutsche.de/img/sz_logo.png", "image/png", "https://rss.sueddeutsche.de/rss/Eilmeldungen",
        news).persistIfNotExist();
    new Feed("Münchner Merkur", "", "", "image/png", "https://www.merkur.de/rssfeed.rdf", news).persistIfNotExist();
    new Feed("Welt: Top-News", "", "", "image/png", "https://www.welt.de/feeds/topnews.rss", news).persistIfNotExist();
    new Feed("Welt: Alle Themen", "", "", "image/png", "https://www.welt.de/feeds/latest.rss",
        news).persistIfNotExist();
    // #############################################################################################################
    Category regionalNews = new Category("Regionale Nachrichten", "Nachrichten aus der Region.");
    regionalNews.persistIfNotExist();
    new Feed("Onetz Amberg", "Regionale Nachrichten aus der Region Amberg",
        "https://www.onetz.de/sites/all/themes/dnt_onet/images/titel-onetz.svg", "image/svg",
        "https://www.onetz.de/themen/Region%20Amberg/index.rss", regionalNews).persistIfNotExist();
    new Feed("Onetz Cham", "Regionale Nachrichten aus der Region Cham",
        "https://www.onetz.de/sites/all/themes/dnt_onet/images/titel-onetz.svg", "image/svg",
        "https://www.onetz.de/themen/Region%20Cham/index.rss", regionalNews).persistIfNotExist();
    new Feed("Onetz Eschenbach", "Regionale Nachrichten aus der Region Eschenbach",
        "https://www.onetz.de/sites/all/themes/dnt_onet/images/titel-onetz.svg", "image/svg",
        "https://www.onetz.de/themen/Region%20Eschenbach/index.rss", regionalNews).persistIfNotExist();
    new Feed("Onetz Kemnath", "Regionale Nachrichten aus der Region Kemnath",
        "https://www.onetz.de/sites/all/themes/dnt_onet/images/titel-onetz.svg", "image/svg",
        "https://www.onetz.de/themen/Region%20Kemnath/index.rss", regionalNews).persistIfNotExist();
    new Feed("Onetz Nabburg", "Regionale Nachrichten aus der Region Schwandorf",
        "https://www.onetz.de/sites/all/themes/dnt_onet/images/titel-onetz.svg", "image/svg",
        "https://www.onetz.de/themen/Region%20Nabburg/rss.xml", regionalNews).persistIfNotExist();
    new Feed("Onetz Neumarkt", "Regionale Nachrichten aus der Region Neumarkt",
        "https://www.onetz.de/sites/all/themes/dnt_onet/images/titel-onetz.svg", "image/svg",
        "https://www.onetz.de/themen/Region%20Neumarkt/rss.xml", regionalNews).persistIfNotExist();
    new Feed("Onetz Oberviechtach", "Regionale Nachrichten aus der Region Oberviechtach",
        "https://www.onetz.de/sites/all/themes/dnt_onet/images/titel-onetz.svg", "image/svg",
        "https://www.onetz.de/themen/Region%20Oberviechtach-Neunburg/rss.xml", regionalNews).persistIfNotExist();
    new Feed("Onetz Regensburg", "Regionale Nachrichten aus der Region Regensburg",
        "https://www.onetz.de/sites/all/themes/dnt_onet/images/titel-onetz.svg", "image/svg",
        "https://www.onetz.de/themen/Region%20Regensburg/rss.xml", regionalNews).persistIfNotExist();
    new Feed("Onetz Schwandorf", "Regionale Nachrichten aus der Region Schwandorf",
        "https://www.onetz.de/sites/all/themes/dnt_onet/images/titel-onetz.svg", "image/svg",
        "https://www.onetz.de/themen/Region%20Schwandorf/rss.xml", regionalNews).persistIfNotExist();
    new Feed("Onetz Sulzbach-Rosenberg", "Regionale Nachrichten aus der Region Sulzbach-Rosenberg",
        "https://www.onetz.de/sites/all/themes/dnt_onet/images/titel-onetz.svg", "image/svg",
        "https://www.onetz.de/themen/Region%20Sulzbach-Rosenberg/rss.xml", regionalNews).persistIfNotExist();
    new Feed("Onetz Tirschenreuth", "Regionale Nachrichten aus der Region Tirschenreuth",
        "https://www.onetz.de/sites/all/themes/dnt_onet/images/titel-onetz.svg", "image/svg",
        "https://www.onetz.de/themen/Region%20Tirschenreuth/rss.xml", regionalNews).persistIfNotExist();
    new Feed("Onetz Vohenstrauß", "Regionale Nachrichten aus der Region Vohenstrauß",
        "https://www.onetz.de/sites/all/themes/dnt_onet/images/titel-onetz.svg", "image/svg",
        "https://www.onetz.de/themen/Region%20Vohenstrau%C3%9F/rss.xml", regionalNews).persistIfNotExist();
    new Feed("Onetz Weiden", "Regionale Nachrichten aus der Region Weiden",
        "https://www.onetz.de/sites/all/themes/dnt_onet/images/titel-onetz.svg", "image/svg",
        "https://www.onetz.de/themen/Region%20Weiden-Neustadt/rss.xml", regionalNews).persistIfNotExist();
    new Feed("Stadt Weiden", "Aktuelles aus der Stadt Weiden in der Oberpfalz",
        "https://www.weiden.de/fileadmin/templates/_images/weiden_wappen.png", "image/svg",
        "https://www.weiden.de/index.php?id=243&type=9818", regionalNews).persistIfNotExist();
    new Feed("Onetz Bayern", "Onetz - Bayern Nachrichten",
        "https://www.onetz.de/sites/all/themes/dnt_onet/images/titel-onetz.svg", "image/svg",
        "https://www.onetz.de/themen/bayern/rss.xml", regionalNews).persistIfNotExist();
    new Feed("Onetz Kultur", "Onetz - Kultur Neuigkeiten",
        "https://www.onetz.de/sites/all/themes/dnt_onet/images/titel-onetz.svg", "image/svg",
        "https://www.onetz.de/themen/kultur/rss.xml", regionalNews).persistIfNotExist();
    new Feed("Onetz Polizeibericht", "Polizeibericht Onetz",
        "https://www.onetz.de/sites/all/themes/dnt_onet/images/titel-onetz.svg", "image/svg",
        "https://www.onetz.de/themen/polizei/rss.xml", regionalNews).persistIfNotExist();
    new Feed("Onetz Oton", "Onetz - Kolumne junger Journalisten der Oberpfalzmedien",
        "https://www.onetz.de/sites/all/themes/dnt_onet/images/titel-onetz.svg", "image/svg",
        "https://www.onetz.de/themen/oton/rss.xml", regionalNews).persistIfNotExist();
    new Feed("Stadt Waldershof", "Aktuelles aus der Stadt Waldershof in der Oberpfalz",
        "https://www.waldershof.de/wp-content/uploads/2015/03/logo_waldershof_retina_2.png", "image/png",
        "https://www.waldershof.de/feed/", regionalNews).persistIfNotExist();
    new Feed("Frankenpost: Topmeldungen", "Topaktuelles aus Franken",
        "https://www.frankenpost.de/www/frankenpost/_responsive/images/logo.svg", "image/svg",
        "https://www.frankenpost.de/topmeldung.rss2.feed", regionalNews).persistIfNotExist();
    new Feed("Frankenpost: Auf einen BLick", "Neueste Meldungen aus Franken auf einen Blick",
        "https://www.frankenpost.de/www/frankenpost/_responsive/images/logo.svg", "image/svg",
        "https://www.frankenpost.de/auf-einen-blick.rss2.feed", regionalNews).persistIfNotExist();
    new Feed("Frankenpost: Regional", "Aktuelle Meldungen aus der Region Franken",
        "https://www.frankenpost.de/www/frankenpost/_responsive/images/logo.svg", "image/svg",
        "https://www.frankenpost.de/region.rss2.feed", regionalNews).persistIfNotExist();
    new Feed("Frankenpost Hof", "Regionale Nachrichten aus der Region Hof",
        "https://www.frankenpost.de/www/frankenpost/_responsive/images/logo.svg", "image/svg",
        "https://www.frankenpost.de/region/hof.rss2.feed", regionalNews).persistIfNotExist();
    new Feed("Frankenpost Rehau", "Regionale Nachrichten aus der Region Rehau",
        "https://www.frankenpost.de/www/frankenpost/_responsive/images/logo.svg", "image/svg",
        "https://www.frankenpost.de/region/rehau.rss2.feed", regionalNews).persistIfNotExist();
    new Feed("Frankenpost Naila", "Regionale Nachrichten aus der Region Naila",
        "https://www.frankenpost.de/www/frankenpost/_responsive/images/logo.svg", "image/svg",
        "https://www.frankenpost.de/region/naila.rss2.feed", regionalNews).persistIfNotExist();
    new Feed("Frankenpost Münchberg", "Regionale Nachrichten aus der Region Münchberg",
        "https://www.frankenpost.de/www/frankenpost/_responsive/images/logo.svg", "image/svg",
        "https://www.frankenpost.de/region/muenchberg.rss2.feed", regionalNews).persistIfNotExist();
    new Feed("Frankenpost Fichtelgebirge", "Regionale Nachrichten aus dem Fichtelgebirge",
        "https://www.frankenpost.de/www/frankenpost/_responsive/images/logo.svg", "image/svg",
        "https://www.frankenpost.de/region/fichtelgebirge.rss2.feed", regionalNews).persistIfNotExist();
    new Feed("Frankenpost Wunsiedel", "Regionale Nachrichten aus der Region Wunsiedel",
        "https://www.frankenpost.de/www/frankenpost/_responsive/images/logo.svg", "image/svg",
        "https://www.frankenpost.de/region/wunsiedel.rss2.feed", regionalNews).persistIfNotExist();
    new Feed("Frankenpost Marktredwitz", "Regionale Nachrichten aus der Region Marktredwitz",
        "https://www.frankenpost.de/www/frankenpost/_responsive/images/logo.svg", "image/svg",
        "https://www.frankenpost.de/region/marktredwitz.rss2.feed", regionalNews).persistIfNotExist();
    new Feed("Frankenpost Selb", "Regionale Nachrichten aus der Region Selb",
        "https://www.frankenpost.de/www/frankenpost/_responsive/images/logo.svg", "image/svg",
        "https://www.frankenpost.de/region/selb.rss2.feed", regionalNews).persistIfNotExist();
    new Feed("Frankenpost Arzberg", "Regionale Nachrichten aus der Region Arzberg",
        "https://www.frankenpost.de/www/frankenpost/_responsive/images/logo.svg", "image/svg",
        "https://www.frankenpost.de/region/arzberg.rss2.feed", regionalNews).persistIfNotExist();
    new Feed("Frankenpost Kulmbach", "Regionale Nachrichten aus der Region Kulmbach",
        "https://www.frankenpost.de/www/frankenpost/_responsive/images/logo.svg", "image/svg",
        "https://www.frankenpost.de/region/kulmbach.rss2.feed", regionalNews).persistIfNotExist();
  }
}
