package de.hablijack.eilkurier.api;

import de.hablijack.eilkurier.entity.Category;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.vertx.mutiny.core.eventbus.EventBus;
import java.util.List;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
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
