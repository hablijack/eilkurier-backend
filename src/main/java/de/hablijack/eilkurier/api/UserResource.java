package de.hablijack.eilkurier.api;

import de.hablijack.eilkurier.entity.User;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.jboss.logging.Logger;

//@RolesAllowed("user")
@Path("api")
@ApplicationScoped
public class UserResource {

  private static final Logger LOGGER = Logger.getLogger(UserResource.class.getName());

  @POST
  @Path("users")
  @SuppressFBWarnings(value = "", justification = "Security is another Epic and on TODO")
  @Transactional
  public User createIfNotExists(User user) {
    Optional<User> userSearch = User.findByEmailOptional(user.email);
    if (userSearch.isEmpty()) {
      user.persist();
      return user;
    } else {
      return userSearch.get();
    }
  }
}
