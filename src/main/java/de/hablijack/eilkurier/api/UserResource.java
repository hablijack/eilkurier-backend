package de.hablijack.eilkurier.api;

import de.hablijack.eilkurier.entity.User;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import java.util.Optional;
import org.jboss.logging.Logger;

//@RolesAllowed("user")
@Path("api")
@ApplicationScoped
public class UserResource {

  private static final Logger LOGGER = Logger.getLogger(UserResource.class.getName());

  @POST
  @Path("users")
  @Consumes(MediaType.APPLICATION_JSON)
  @SuppressFBWarnings(value = "", justification = "Security is another Epic and on TODO")
  @Transactional
  public User createIfNotExists(User user) {
    LOGGER.info("Try to find user by email..");
    Optional<User> userSearch = User.findByEmailOptional(user.email);
    if (userSearch.isEmpty()) {
      LOGGER.info("User not found, creating new one...");
      user.persist();
      return user;
    } else {
      LOGGER.info("User found - returning it...");
      return userSearch.get();
    }
  }
}
