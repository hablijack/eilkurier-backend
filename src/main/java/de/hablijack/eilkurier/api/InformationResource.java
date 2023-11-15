package de.hablijack.eilkurier.api;

import de.hablijack.eilkurier.entity.Information;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import java.util.List;

@Path("api")
@ApplicationScoped
public class InformationResource {

  @GET
  @Path("categories/feeds/information")
  @SuppressFBWarnings(value = "", justification = "Security is another Epic and on TODO")
  public List<Information> getInformationByFeedId() {
    return Information.listAll();
  }
}
