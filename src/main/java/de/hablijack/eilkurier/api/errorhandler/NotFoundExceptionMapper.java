package de.hablijack.eilkurier.api.errorhandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.apache.http.HttpStatus;
import org.jboss.logging.Logger;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

  private static final Logger LOGGER = Logger.getLogger(NotFoundExceptionMapper.class.getName());

  @Override
  public Response toResponse(NotFoundException exception) {
    LOGGER.error(exception);
    String responseJSON = "";
    try {
      responseJSON = new ObjectMapper().writeValueAsString("");
    } catch (JsonProcessingException e) {
      responseJSON = "";
    }
    return Response.status(HttpStatus.SC_NOT_FOUND).entity(responseJSON).type(MediaType.APPLICATION_JSON).build();
  }
}
