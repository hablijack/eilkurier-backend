package de.hablijack.eilkurier.service;

import de.hablijack.eilkurier.entity.Feed;
import de.hablijack.eilkurier.entity.Information;
import de.hablijack.eilkurier.entity.RssItem;
import de.hablijack.eilkurier.enumeration.RSSTag;
import io.quarkus.vertx.ConsumeEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
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

  private static final Logger LOGGER = Logger.getLogger(FeedService.class.getName());

  @Transactional
  @ConsumeEvent(value = "fetch_feed_information", blocking = true)
  public void fetchFeedInformation(Feed feed) throws IOException, XMLStreamException {
    RssItem item = new RssItem();
    XMLInputFactory inputFactory = XMLInputFactory.newInstance();
    InputStream in = new URL(feed.url).openStream();
    XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
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
          event = eventReader.nextEvent();
        } else if (localPart.contains(RSSTag.TITLE.getName())) {
          item.title = getCharacterData(event, eventReader);
        } else if (localPart.contains(RSSTag.DESCRIPTION.getName())) {
          item.description = getCharacterData(event, eventReader);
        } else if (localPart.contains(RSSTag.LINK.getName())) {
          item.link = getCharacterData(event, eventReader);
        } else if (localPart.contains(RSSTag.GUID.getName())) {
          item.guid = getCharacterData(event, eventReader);
        } else if (localPart.contains(RSSTag.LANGUAGE.getName())) {
          item.language = getCharacterData(event, eventReader);
        } else if (localPart.contains(RSSTag.AUTHOR.getName())) {
          item.author = getCharacterData(event, eventReader);
        } else if (localPart.contains(RSSTag.PUB_DATE.getName())) {
          item.pubdate = getCharacterData(event, eventReader);
        } else if (localPart.contains(RSSTag.COPYRIGHT.getName())) {
          item.copyright = getCharacterData(event, eventReader);
        } else if (localPart.contains(RSSTag.CONTENT.getName())) {
          item.content = getCharacterData(event, eventReader);
        } else if (localPart.contains(RSSTag.ENCLOSURE.getName())) {
          item.enclosure = getCharacterData(event, eventReader);
        }
      } else if (event.isEndElement()) {
        if (event.asEndElement().getName().getLocalPart().contains(RSSTag.ITEM.getName())) {
          if (item.hasGUID() && !Information.existsByGuidAndFeed(item.guid, feed)) {
            Information info = new Information();
            info.feed = feed;
            if (item.author.isEmpty()) {
              info.author = feed.name;
            } else {
              info.author = item.author;
            }
            String message = "";
            if (item.hasContent()) {
              message = item.content;
            } else {
              message = item.description;
            }
            if (message.length() > 1200) {
              message = truncateMessageBody(message, 1195) + " (... weiterlesen ...)";
            } else if (message.length() < 20) {
              continue;
            }
            info.message = message;
            if (item.hasEnclusure()) {
              info.picture = item.enclosure;
            } else {
              Pattern regexImagePattern = Pattern.compile("src=\"(.*?)\"");
              Matcher imageMatcher = regexImagePattern.matcher(message);
              if (imageMatcher.find()) {
                if (imageMatcher.group(1).contains("http")) {
                  info.picture = imageMatcher.group(1);
                }
              }
            }
            info.textonlymessage = Jsoup.clean(message, Whitelist.simpleText());
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
          event = eventReader.nextEvent();
          continue;
        }
      }
    }
  }

  private String getCharacterData(XMLEvent event, XMLEventReader eventReader)
      throws XMLStreamException {
    String result = "";
    event = eventReader.nextEvent();
    if (event instanceof Characters) {
      result = event.asCharacters().getData();
    }
    return result;
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
      }
    }
    if (!inTag) {
      lastTagStart = size;
    }
    return input.substring(0, lastTagStart);
  }
}
