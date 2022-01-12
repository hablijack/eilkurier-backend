package de.hablijack.eilkurier.api;

import de.hablijack.eilkurier.entity.Subscription;
import de.hablijack.eilkurier.entity.User;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.jboss.logging.Logger;

//@RolesAllowed("user")
@Path("api")
@ApplicationScoped
public class SubscriptionResource {

  private static final Logger LOGGER = Logger.getLogger(SubscriptionResource.class.getName());

  @POST
  @Path("categories/feeds/subscriptions/bulk")
  @SuppressFBWarnings(value = "", justification = "Security is another Epic and on TODO")
  @Transactional
  public void createSubscription(List<Subscription> subscriptions) {
    User christoph = User.findById(66);
    for (Subscription subscription : subscriptions) {
      subscription.user = christoph;
      subscription.persist();
    }
  }
}
