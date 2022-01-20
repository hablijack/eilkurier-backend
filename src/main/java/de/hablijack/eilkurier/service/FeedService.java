package de.hablijack.eilkurier.service;

import de.hablijack.eilkurier.entity.Feed;
import de.hablijack.eilkurier.entity.Information;
import de.hablijack.eilkurier.entity.RssItem;
import de.hablijack.eilkurier.enumeration.RSSTag;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.quarkus.vertx.ConsumeEvent;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import org.jboss.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

@ApplicationScoped
public class FeedService {

  public static final int MESSAGE_MAX_LENGTH = 1200;
  public static final int MESSAGE_CUT_LENGTH = 1195;
  public static final int MESSAGE_MIN_LENGTH = 20;

  private static final Logger LOGGER = Logger.getLogger(FeedService.class.getName());

  @Transactional
  @ConsumeEvent(value = "fetch_feed_information", blocking = true)
  @SuppressFBWarnings(value = "URLCONNECTION_SSRF_FD", justification = "we validated our URLs often enough...")
  // TODO: Organize Method with private helpers
  public void fetchFeedInformation(Feed feed) throws IOException, XMLStreamException {
    RssItem item = new RssItem();
    XMLInputFactory inputFactory = XMLInputFactory.newInstance();
    inputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
    inputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
    URL url = new URL(feed.url);
    InetAddress inetAddress = InetAddress.getByName(url.getHost());
    if (!url.getProtocol().startsWith("https") || inetAddress.isAnyLocalAddress() || inetAddress.isLoopbackAddress()
        || inetAddress.isLinkLocalAddress()) {
      throw new IOException("Attack dected!");
    }

    HttpURLConnection conn = (HttpURLConnection) (url.openConnection());
    conn.setInstanceFollowRedirects(false);
    conn.connect();
    XMLEventReader eventReader = inputFactory.createXMLEventReader(conn.getInputStream());
    while (eventReader.hasNext()) {
      XMLEvent event = eventReader.nextEvent();
      if (event.isStartElement()) {
        String localPart = event.asStartElement().getName().getLocalPart();
        if (localPart.equals("encoded")) {
          localPart = event.asStartElement().getName().getPrefix();
        }
        if (localPart.contains(RSSTag.ITEM.getName())) {
          if (item.isFeedHeader) {
            item.isFeedHeader = false;
          }
        } else if (localPart.contains(RSSTag.TITLE.getName())) {
          item.title = getCharacterData(eventReader);
        } else if (localPart.contains(RSSTag.DESCRIPTION.getName())) {
          item.description = getCharacterData(eventReader);
        } else if (localPart.contains(RSSTag.LINK.getName())) {
          item.link = getCharacterData(eventReader);
        } else if (localPart.contains(RSSTag.GUID.getName())) {
          item.guid = getCharacterData(eventReader);
        } else if (localPart.contains(RSSTag.LANGUAGE.getName())) {
          item.language = getCharacterData(eventReader);
        } else if (localPart.contains(RSSTag.AUTHOR.getName())) {
          item.author = getCharacterData(eventReader);
        } else if (localPart.contains(RSSTag.PUB_DATE.getName())) {
          item.pubdate = getCharacterData(eventReader);
        } else if (localPart.contains(RSSTag.COPYRIGHT.getName())) {
          item.copyright = getCharacterData(eventReader);
        } else if (localPart.contains(RSSTag.CONTENT.getName())) {
          item.content = getCharacterData(eventReader);
        } else if (localPart.contains(RSSTag.ENCLOSURE.getName())) {
          item.enclosure = getCharacterData(eventReader);
        }
      } else if (event.isEndElement()
          && event.asEndElement().getName().getLocalPart().contains(RSSTag.ITEM.getName())) {

        if (item.hasGUID() && !Information.existsByGuidAndFeed(item.guid, feed)) {
          Information info = new Information();
          info.feed = feed;
          if (item.author == null || item.author.isEmpty()) {
            info.author = feed.name;
          } else {
            info.author = item.author;
          }
          String message = "";
          if (item.hasContent()) {
            message = item.content;
          }

          if (message.length() < MESSAGE_MIN_LENGTH) {
            continue;
          }
          info.message = message;

          Pattern regexImagePattern = Pattern.compile("src=\"(.*?)\"");
          Matcher imageMatcher = regexImagePattern.matcher(message);
          List<String> images = new ArrayList<>();
          while (imageMatcher.find()) {
            if (imageMatcher.group(1).contains("http")) {
              images.add(imageMatcher.group(1));
            }
          }
          info.pictures = String.join("||", images);

          String textonlymessage = message.replace("<![CDATA", "").replace("]]", "");
          info.textonlymessage = Jsoup.clean(textonlymessage, Whitelist.simpleText());
          info.guid = item.guid;
          info.link = item.link;
          info.title = item.title;
          try {
            info.timestamp = item.getPubdate();
          } catch (Exception ex) {
            info.timestamp = new Date();
            LOGGER.error(item.pubdate);
          }
          info.feed.language = item.language;
          info.feed.copyright = item.copyright;
          info.persist();
        }
      }
    }
    conn.disconnect();
  }

  private String getCharacterData(XMLEventReader eventReader) throws XMLStreamException {
    XMLEvent event = eventReader.nextEvent();
    if (event instanceof Characters) {
      return event.asCharacters().getData().trim();
    } else {
      return "";
    }
  }

  private String truncateMessageBody(String input, int size) {
    if (input.length() < size) {
      return input;
    }
    int lastTagStart = 0;
    boolean inString = false;
    boolean inTag = false;
    for (int pos = 0; pos < size; pos++) {
      switch (input.charAt(pos)) {
        case '<':
          if (!inString && !inTag) {
            lastTagStart = pos;
            inTag = true;
          }
          break;
        case '>':
          if (!inString) {
            inTag = false;
          }
          break;
        case '\"':
          if (inTag) {
            inString = !inString;
          }
          break;
        default:
          break;
      }
    }
    if (!inTag) {
      lastTagStart = size;
    }
    return input.substring(0, lastTagStart);
  }
}
