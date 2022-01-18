package de.hablijack.eilkurier.api.errorhandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import org.apache.http.HttpStatus;
import org.jboss.logging.Logger;


public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {

  private static final Logger LOGGER = Logger.getLogger(IllegalArgumentExceptionMapper.class.getName());

  @Override
  public Response toResponse(IllegalArgumentException exception) {
    LOGGER.error(exception);
    String responseJSON = "";
    try {
      responseJSON = new ObjectMapper().writeValueAsString("");
    } catch (JsonProcessingException e) {
      responseJSON = "";
    }
    return Response.status(HttpStatus.SC_BAD_REQUEST).entity(responseJSON).type(MediaType.APPLICATION_JSON).build();
  }
}
