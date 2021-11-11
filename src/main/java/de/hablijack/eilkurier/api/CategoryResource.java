package de.hablijack.eilkurier.api;

import de.hablijack.eilkurier.entity.Category;
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
public class CategoryResource {

  @POST
  @Path("categories")
  public void insert(Category category) {
    category.persist();
  }

  @DELETE
  @Path("category/{id}")
  public void delete(@PathParam("id") String id) {
    Category.findById(id).delete();
  }

  @PUT
  @Path("category/{id}")
  public void update(@PathParam("id") String id, Category newCategory) {
    Category oldCategory = Category.findById(id);
    oldCategory.description = newCategory.description;
    oldCategory.name = newCategory.name;
    oldCategory.feeds = newCategory.feeds;
    oldCategory.persist();
  }

  @GET
  @Path("categories")
  @Produces(MediaType.APPLICATION_JSON)
  public List<Category> get() {
    return Category.findAll().list();
  }

  @GET
  @Path("category/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Category getOne(@PathParam("id") String id) {
    return Category.findById(id);
  }
}
