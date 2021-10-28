package de.hablijack.eilkurier.api;

import de.hablijack.eilkurier.entity.Feed;
import de.hablijack.eilkurier.entity.Information;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("api")
@ApplicationScoped
public class FeedResource {

    @GET
    @Path("feeds")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Information> getAllFeeds() {
        return Feed.findAll().list();
    }
}
