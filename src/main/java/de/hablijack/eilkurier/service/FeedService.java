package de.hablijack.eilkurier.service;

import com.rometools.rome.feed.synd.SyndEntryImpl;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import de.hablijack.eilkurier.entity.Feed;
import de.hablijack.eilkurier.entity.Information;
import io.quarkus.vertx.ConsumeEvent;
import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import org.jboss.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

@ApplicationScoped
public class FeedService {
  private static final Logger LOGGER = Logger.getLogger(FeedService.class.getName());

  @ConsumeEvent(value = "fetch_feed_information", blocking = true)
  @Transactional
  public void fetchFeedInformation(Feed feed) {
    LOGGER.debug("Fetching Feed: " + feed.name);
    try {
      URL feedSource = null;

      feedSource = new URL(feed.url);

      SyndFeedInput input = new SyndFeedInput();
      input.setAllowDoctypes(true);
      input.setXmlHealerOn(true);
      SyndFeed xml = null;
      xml = input.build(new XmlReader(feedSource));

      for (Object entry : xml.getEntries()) {
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
              message = Jsoup.clean(message, Safelist.basicWithImages());
            } else {
              if (currentEntry.getDescription() == null) {
                continue;
              } else {
                message = currentEntry.getDescription().getValue();
                message = Jsoup.clean(message, Safelist.basicWithImages());
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
            info.textonlymessage = Jsoup.clean(message, Safelist.basic());
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
    } catch (FeedException | IOException ex) {
      LOGGER.error("Error on fetching Feed: " + feed.name + " - : " + ex.getMessage());
    }
  }

  private boolean hasContent(SyndEntryImpl entry) {
    return (entry.getContents().size() > 0 && entry.getContents().iterator().next().getValue().trim().length() > 0);
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
