package de.hablijack.eilkurier.api;

import de.hablijack.eilkurier.entity.Category;
import de.hablijack.eilkurier.entity.Information;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@ApplicationScoped
public class CategoryResource {

  @GET
  @Path("categories")
  @Produces(MediaType.APPLICATION_JSON)
  public List<Information> getAllCategories() {
    return Category.findAll().list();
  }
}
