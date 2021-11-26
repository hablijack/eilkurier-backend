package de.hablijack.eilkurier.api;

import de.hablijack.eilkurier.entity.Feed;
import io.vertx.mutiny.core.eventbus.EventBus;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.jboss.logging.Logger;

//@RolesAllowed("user")
@Path("api")
@ApplicationScoped
public class FeedResource {

  private static final Logger LOGGER = Logger.getLogger(FeedResource.class.getName());

  @Inject
  EventBus eventBus;

  @POST
  @Path("feeds/import")
  public void startImport() {
    LOGGER.info("Starting to fetch Feeds...");
    List<Feed> allFeeds = Feed.listAll();
    for (Feed feed : allFeeds) {
      eventBus.send("fetch_feed_information", feed);
    }
    LOGGER.info("Feeds fetched successfully.");
  }
}
