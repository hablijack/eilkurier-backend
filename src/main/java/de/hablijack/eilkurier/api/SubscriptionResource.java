package de.hablijack.eilkurier.api;

import de.hablijack.eilkurier.entity.Feed;
import de.hablijack.eilkurier.entity.Subscription;
import de.hablijack.eilkurier.entity.User;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.quarkus.oidc.IdToken;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

//@RolesAllowed("user")
@Path("api")
@ApplicationScoped
public class SubscriptionResource {

  private static final Logger LOGGER = Logger.getLogger(SubscriptionResource.class.getName());

  @Inject
  @IdToken
  JsonWebToken idToken;

  @POST
  @Path("categories/feeds/subscriptions/bulk")
  @Consumes(MediaType.APPLICATION_JSON)
  @SuppressFBWarnings(value = "", justification = "Security is another Epic and on TODO")
  @Transactional
  public List<Subscription> createSubscription(List<Feed> feeds) {
    List subscriptions = new ArrayList();
    LOGGER.info("Trying to load current user...");
    String email = idToken.getClaim("email");
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
    String email = idToken.getClaim("email");
    User christoph = User.findByEmailOptional(email).get();
    LOGGER.info("Trying to load subscriptions for user...");
    return Subscription.findByUser(christoph);
  }
}
