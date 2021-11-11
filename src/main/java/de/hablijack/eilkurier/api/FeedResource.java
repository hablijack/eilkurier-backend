package de.hablijack.eilkurier.api;

import de.hablijack.eilkurier.entity.Feed;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

//@RolesAllowed("user")
@Path("api")
@ApplicationScoped
public class FeedResource {

  @POST
  @Path("categories")
  public void insert(Feed feed) {
    feed.persist();
  }

  @DELETE
  @Path("feed/{id}")
  public void delete(@PathParam("id") String id) {
    Feed.findById(id).delete();
  }

  @PUT
  @Path("feed/{id}")
  public void update(@PathParam("id") String id, Feed newFeed) {
    Feed oldFeed = Feed.findById(id);
    oldFeed.name = newFeed.name;
    oldFeed.pictureContentType = newFeed.pictureContentType;
    oldFeed.picture = newFeed.picture;
    oldFeed.url = newFeed.url;
    oldFeed.category = newFeed.category;
    oldFeed.description = newFeed.description;
    oldFeed.persist();
  }

  @GET
  @Path("feeds")
  @Produces(MediaType.APPLICATION_JSON)
  public List<Feed> get() {
    return Feed.findAll().list();
  }

  @GET
  @Path("feed/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Feed getOne(@PathParam("id") String id) {
    return Feed.findById(id);
  }
}
