package de.hablijack.eilkurier.service;

import de.hablijack.eilkurier.entity.Feed;
import de.hablijack.eilkurier.entity.Information;
import io.quarkus.vertx.ConsumeEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import org.jboss.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.xml.sax.SAXException;


@ApplicationScoped
public class FeedService {
  private static final String TITLE = "title";
  private static final String DESCRIPTION = "description";
  private static final String CONTENT = "content";
  private static final String CHANNEL = "channel";
  private static final String LANGUAGE = "language";
  private static final String COPYRIGHT = "copyright";
  private static final String LINK = "link";
  private static final String AUTHOR = "author";
  private static final String ITEM = "item";
  private static final String PUB_DATE = "pubDate";
  private static final String GUID = "guid";
  private static final String ENCLOSURE = "enclosure";

  private static final Logger LOGGER = Logger.getLogger(FeedService.class.getName());

  private String getCharacterData(XMLEvent event, XMLEventReader eventReader)
      throws XMLStreamException {
    String result = "";
    event = eventReader.nextEvent();
    if (event instanceof Characters) {
      result = event.asCharacters().getData();
    }
    return result;
  }

  @ConsumeEvent(value = "fetch_feed_information", blocking = true)
  @Transactional
  public void fetchFeedInformation(Feed feed)
      throws IOException, ParserConfigurationException, SAXException, XMLStreamException, ParseException {
    boolean isFeedHeader = true;
    String description = "";
    String content = "";
    String title = "";
    String link = "";
    String language = "";
    String copyright = "";
    String author = "";
    String pubdate = "";
    String guid = "";
    String enclosure = "";
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
        if (localPart.contains(ITEM)) {
          if (isFeedHeader) {
            isFeedHeader = false;
          }
          event = eventReader.nextEvent();
        } else if (localPart.contains(TITLE)) {
          title = getCharacterData(event, eventReader);
        } else if (localPart.contains(DESCRIPTION)) {
          description = getCharacterData(event, eventReader);
        } else if (localPart.contains(LINK)) {
          link = getCharacterData(event, eventReader);
        } else if (localPart.contains(GUID)) {
          guid = getCharacterData(event, eventReader);
        } else if (localPart.contains(LANGUAGE)) {
          language = getCharacterData(event, eventReader);
        } else if (localPart.contains(AUTHOR)) {
          author = getCharacterData(event, eventReader);
        } else if (localPart.contains(PUB_DATE)) {
          pubdate = getCharacterData(event, eventReader);
        } else if (localPart.contains(COPYRIGHT)) {
          copyright = getCharacterData(event, eventReader);
        } else if (localPart.contains(CONTENT)) {
          content = getCharacterData(event, eventReader);
        } else if (localPart.contains(ENCLOSURE)) {
          enclosure = getCharacterData(event, eventReader);
        }
      } else if (event.isEndElement()) {
        if (event.asEndElement().getName().getLocalPart() == (ITEM)) {
          if (guid != null && guid.length() > 0 && !Information.existsByGuidAndFeed(guid, feed)) {
            Information info = new Information();
            info.feed = feed;
            if (author.isEmpty()) {
              info.author = feed.name;
            } else {
              info.author = author;
            }
            String message = "";
            if (content != null && content.length() > 0) {
              message = content;
            } else {
              message = description;
            }
            if (message.length() > 1200) {
              message = truncateMessageBody(message, 1195) + " (... weiterlesen ...)";
            } else if (message.length() < 20) {
              continue;
            }
            info.message = message;
            if (enclosure.length() > 0) {
              info.picture = enclosure;
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
            info.guid = guid;
            info.link = link;
            info.title = title;
            try {
              info.timestamp = Date.from(Instant.from(DateTimeFormatter.RFC_1123_DATE_TIME.parse(pubdate)));
            } catch (Exception ex) {
              info.timestamp = new Date();
              LOGGER.error(pubdate);
            }
            info.feed.language = language;
            info.feed.copyright = copyright;
            info.persist();
          }
          event = eventReader.nextEvent();
          continue;
        }
      }
    }


    /*LOGGER.debug("Fetching Feed: " + feed.name);
    //try {
    FeedFetcher feedFetcher = new HttpClientFeedFetcher();
    SyndFeed feed = feedFetcher.retrieveFeed("your-rss-reader-user-agent", new URL(feed.url));

    for (Object entry : feed.getEntries()) {
      SyndEntryImpl currentEntry = (SyndEntryImpl) entry;
      if (currentEntry.getTitleEx() != null &&
          !Information.existsByTitleAndFeed(currentEntry.getTitleEx().getValue(), feed)) {
        LOGGER.debug("Fetching Information from feed.");
        try {
          Information info = new Information();
          info.feed = feed;
          if (currentEntry.getAuthor().isEmpty()) {
            info.author = feed.name;
          } else {
            info.author = currentEntry.getAuthor();
          }
          String message = "";
          if (hasContent(currentEntry)) {
            message = currentEntry.getContents().iterator().next().getValue();
            message = Jsoup.clean(message, Whitelist.basicWithImages());
          } else {
            if (currentEntry.getDescription() == null) {
              continue;
            } else {
              message = currentEntry.getDescription().getValue();
              message = Jsoup.clean(message, Whitelist.basicWithImages());
            }
          }
          if (message.length() > 1200) {
            message = truncateMessageBody(message, 1195) + " (... weiterlesen ...)";
          } else if (message.length() < 20) {
            continue;
          }
          info.message = message;

          Pattern regexImagePattern = Pattern.compile("src=\"(.*?)\"");
          Matcher imageMatcher = regexImagePattern.matcher(message);
          if (imageMatcher.find()) {
            if (imageMatcher.group(1).contains("http")) {
              info.picture = imageMatcher.group(1);
            }
          }
          info.textonlymessage = Jsoup.clean(message, Whitelist.simpleText());
          info.title = currentEntry.getTitleEx().getValue();
          info.timestamp = currentEntry.getPublishedDate();
          if (currentEntry.getLink() == null || currentEntry.getLink().length() <= 0) {
            info.link = currentEntry.getUri();
          } else {
            info.link = currentEntry.getLink();
          }
          LOGGER.debug("Saving Information,");
          info.persistAndFlush();
        } catch (Exception ex) {
          LOGGER.error("Error on fetching Information from feed: " + feed.name + " - : " + ex.getMessage());
        }
      } else {
        LOGGER.debug("Information already exists! Ignoring.");
      }
    }
    LOGGER.debug("Feed: " + feed.name + " fetched successfully.");
   /* } catch (FeedException | IOException ex) {
      LOGGER.error("Error on fetching Feed: " + feed.name + " - : " + ex.getMessage());
    }
    */
  }

  /*private boolean hasContent(SyndEntryImpl entry) {
    return (entry.getContents().size() > 0 && entry.getContents().iterator().next().getValue().trim().length() > 0);
  }*/

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
