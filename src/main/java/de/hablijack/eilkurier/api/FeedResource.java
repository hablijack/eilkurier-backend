package de.hablijack.eilkurier.api;

import de.hablijack.eilkurier.entity.Feed;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import java.io.IOException;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import org.jboss.logging.Logger;

//@RolesAllowed("user")
@Path("api")
@ApplicationScoped
public class FeedResource {

  private static final Logger LOGGER = Logger.getLogger(FeedResource.class.getName());

  @Inject
  EventBus eventBus;

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
  public String startImport() throws XMLStreamException, IOException {
    LOGGER.info("Starting to fetch Feeds...");
    List<Feed> allFeeds = Feed.listAll();
    for (Feed feed : allFeeds) {
      eventBus.send("fetch_feed_information", feed);
    }
    LOGGER.info("Feeds fetched successfully.");
    return "";
  }
}
