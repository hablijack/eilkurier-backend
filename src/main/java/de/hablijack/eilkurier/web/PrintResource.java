package de.hablijack.eilkurier.web;

import de.hablijack.eilkurier.entity.Information;
import de.hablijack.eilkurier.entity.Subscription;
import de.hablijack.eilkurier.entity.User;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Path("print")
public class PrintResource {

  @Location("print.html")
  Template printedEilkurierTemplate;

  @GET
  @Produces(MediaType.TEXT_HTML)
  @SuppressFBWarnings(value = "DLS_DEAD_LOCAL_STORE")
  public TemplateInstance get(@QueryParam("userId") Long userId) {
    List<Information> information = new ArrayList<>();
    Set<String> categoryNames = new HashSet<>();
    User user = User.findById(userId);
    List<Subscription> subscriptions = Subscription.findByUser(user);
    for (Subscription subscription : subscriptions) {
      information.addAll(Information.findByFeed(subscription.feed));
      categoryNames.add(subscription.feed.category.name);
    }
    return printedEilkurierTemplate
        .data("information", information)
        .data("categoryNames", categoryNames);
  }
}
