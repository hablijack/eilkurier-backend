package de.hablijack.eilkurier.api;

import io.quarkus.security.identity.SecurityIdentity;
import java.util.Map;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.jboss.resteasy.annotations.cache.NoCache;

@Path("/api/users")
@ApplicationScoped
public class UserResource {

  @Inject
  SecurityIdentity securityIdentity;

  @GET
  @Path("/me")
  @RolesAllowed("user")
  @NoCache
  public Map me() {
    return securityIdentity.getAttributes();
  }
}