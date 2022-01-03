package de.hablijack.eilkurier.api;

import de.hablijack.eilkurier.entity.Category;
import de.hablijack.eilkurier.entity.Feed;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.vertx.mutiny.core.eventbus.EventBus;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.jboss.logging.Logger;

//@RolesAllowed("user")
@Path("api")
@ApplicationScoped
public class CategoryResource {

  private static final Logger LOGGER = Logger.getLogger(CategoryResource.class.getName());

  @Inject
  EventBus eventBus;

  @GET
  @Path("categories")
  @SuppressFBWarnings(value = "", justification = "Security is another Epic and on TODO")
  public List<Category> getAllCategories() {
    LOGGER.info("Starting to fetch Categories...");
    return Category.listAll();
  }
}
