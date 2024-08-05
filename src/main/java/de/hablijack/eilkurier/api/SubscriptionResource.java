package de.hablijack.eilkurier.api;

import de.hablijack.eilkurier.entity.Feed;
import de.hablijack.eilkurier.entity.Subscription;
import de.hablijack.eilkurier.entity.User;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import org.jboss.logging.Logger;

//@RolesAllowed("user")
@Path("api")
@ApplicationScoped
public class SubscriptionResource {

  private static final Logger LOGGER = Logger.getLogger(SubscriptionResource.class.getName());

  @Inject
  SecurityIdentity securityIdentity;

  @POST
  @Path("categories/feeds/subscriptions/bulk")
  @Consumes(MediaType.APPLICATION_JSON)
  @SuppressFBWarnings(value = "", justification = "Security is another Epic and on TODO")
  @Transactional
  public List<Subscription> createSubscription(List<Feed> feeds) {
    List<Subscription> subscriptions = new ArrayList<>();
    LOGGER.info("Trying to load current user...");
    String email = securityIdentity.getPrincipal().getName();
    User christoph = User.findByEmailOptional(email).get();
    LOGGER.info("Looping feeds and generating subscriptions...");
    for (Feed feed : feeds) {
      Subscription subscription = new Subscription();
      subscription.feed = feed;
      subscription.user = christoph;
      LOGGER.info("Subscription generated - storing in DB...");
      subscription.persistIfNotExist();
      subscriptions.add(subscription);
    }
    LOGGER.info("Subscriptions successfully generated, returning result...");
    return subscriptions;
  }

  @GET
  @Path("categories/feeds/subscriptions/byUser")
  @SuppressFBWarnings(value = "", justification = "Security is another Epic and on TODO")
  public List<Subscription> getByUser() {
    LOGGER.info("Trying to load current user...");
    String email = securityIdentity.getPrincipal().getName();
    User christoph = User.findByEmailOptional(email).get();
    LOGGER.info("Trying to load subscriptions for user...");
    return Subscription.findByUser(christoph);
  }
}
