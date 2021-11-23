package de.hablijack.eilkurier.graphql;

import de.hablijack.eilkurier.entity.Information;
import java.util.List;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;

//@RolesAllowed("user")
@GraphQLApi
public class InformationResource {

  @Query("information")
  @Description("Get all Information stored in database")
  public List<Information> getAllInformation() {
    return Information.findAll().list();
  }

  @Query("informationCount")
  @Description("Get count of all Information stored in database")
  public int getCount() {
    return Information.findAll().list().size();
  }
}
