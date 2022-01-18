package de.hablijack.eilkurier.web;

import de.hablijack.eilkurier.entity.Information;
import de.hablijack.eilkurier.entity.Subscription;
import de.hablijack.eilkurier.entity.User;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("print")
public class PrintResource {

  @Location("print.html")
  Template printedEilkurierTemplate;

  @GET
  @Produces(MediaType.TEXT_HTML)
  public TemplateInstance get() {
    List<Information> information = new ArrayList();
    Set<String> categoryNames = new HashSet();
    User christoph = User.findByEmailOptional("christoph.habel@posteo.de").get();
    List<Subscription> subscriptions = Subscription.findByUser(christoph);
    for (Subscription subscription : subscriptions) {
      information.addAll(Information.findByFeed(subscription.feed));
      categoryNames.add(subscription.feed.category.name);
    }
    return printedEilkurierTemplate
        .data("information", information)
        .data("categoryNames", categoryNames);
  }
}