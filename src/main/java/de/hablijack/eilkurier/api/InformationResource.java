package de.hablijack.eilkurier.api;

import de.hablijack.eilkurier.entity.Information;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("api/information")
@ApplicationScoped
public class InformationResource {
    private static final Logger LOGGER = Logger.getLogger(InformationResource.class.getName());

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Information> getAllInformation() {
        return Information.findAll().list();
    }

    @GET
    @Path("counter")
    @Produces(MediaType.APPLICATION_JSON)
    public int getCount() {
        return Information.findAll().list().size();
    }
}
