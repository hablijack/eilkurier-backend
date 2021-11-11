package de.hablijack.eilkurier.api;

import de.hablijack.eilkurier.entity.Information;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.jboss.logging.Logger;

//@RolesAllowed("user")
@Path("api/information")
@ApplicationScoped
public class InformationResource {
  private static final Logger LOGGER = Logger.getLogger(InformationResource.class.getName());

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<Information> get() {
    return Information.findAll().list();
  }

  @GET
  @Path("counter")
  @Produces(MediaType.APPLICATION_JSON)
  public int getCount() {
    return Information.findAll().list().size();
  }

  @DELETE
  @Path("{id}")
  public void delete(@PathParam("id") String id) {
    Information.findById(id).delete();
  }
}
