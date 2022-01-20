package de.hablijack.eilkurier.api;

import de.hablijack.eilkurier.entity.Feed;
import de.hablijack.eilkurier.service.FeedService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.xml.stream.XMLStreamException;
import org.jboss.logging.Logger;

//@RolesAllowed("user")
@Path("api")
@ApplicationScoped
public class FeedResource {

  private static final Logger LOGGER = Logger.getLogger(FeedResource.class.getName());

  /*@Inject
  EventBus eventBus;*/
  @Inject
  FeedService feedService;

  @GET
  @Path("categories/feeds")
  @SuppressFBWarnings(value = "", justification = "Security is another Epic and on TODO")
  public List<Feed> getFeedsByCategoryIds(@QueryParam("categoryId") List<Long> categoryIds) {
    LOGGER.info("Try to find Categories for Feeds: " + categoryIds);
    return Feed.finByCategoryIds(categoryIds);
  }

  @GET
  @Path("categories/feeds/import")
  @SuppressFBWarnings(value = "", justification = "Security is another Epic and on TODO")
  public void startImport() throws XMLStreamException, IOException {
    LOGGER.info("Starting to fetch Feeds...");
    List<Feed> allFeeds = Feed.listAll();
    for (Feed feed : allFeeds) {
      //eventBus.send("fetch_feed_information", feed);
      feedService.fetchFeedInformation(feed);
    }
    LOGGER.info("Feeds fetched successfully.");
  }
}
