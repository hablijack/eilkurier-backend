package de.hablijack.eilkurier.api.errorhandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.jboss.logging.Logger;

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
    return Response.status(Response.Status.NOT_FOUND).entity(responseJSON).type(MediaType.APPLICATION_JSON).build();
  }
}
