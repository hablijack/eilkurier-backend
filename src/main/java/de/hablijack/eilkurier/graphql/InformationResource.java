package de.hablijack.eilkurier.graphql;

import de.hablijack.eilkurier.entity.Information;
import java.util.List;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;

//@RolesAllowed("user")
@GraphQLApi
public class InformationResource {

  @Query("information")
  @Description("Get one Information stored in database")
  public Information getInformation(int id) {
    return Information.findById(id);
  }

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

  @Mutation
  public Information createInformation(Information info) {
    info.persist();
    return info;
  }

  @Mutation
  public Information deleteInformation(int id) {
    Information info = Information.findById(id);
    info.delete();
    return info;
  }
}
