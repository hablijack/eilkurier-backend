package de.hablijack.eilkurier.service;

import de.hablijack.eilkurier.entity.Feed;
import io.quarkus.scheduler.Scheduled;
import io.vertx.mutiny.core.eventbus.EventBus;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import org.jboss.logging.Logger;
import org.xml.sax.SAXException;

@ApplicationScoped
public class CronService {

  private static final Logger LOGGER = Logger.getLogger(CronService.class.getName());

  @Inject
  FeedService feedService;

  @Inject
  EventBus eventBus;

  @Scheduled(every = "10m")
  public void fetchInfos()
      throws XMLStreamException, IOException, ParserConfigurationException, ParseException, SAXException {
    LOGGER.info("Starting to fetch Feeds...");
    List<Feed> allFeeds = Feed.listAll();
    for (Feed feed : allFeeds) {
      //feedService.fetchFeedInformation(feed);
      eventBus.send("fetch_feed_information", feed);
    }
    LOGGER.info("Feeds fetched successfully.");
  }
}
