package de.hablijack.eilkurier.api.errorhandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.apache.http.HttpStatus;

@Provider
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {

  @Override
  public Response toResponse(IllegalArgumentException exception) {

    String responseJSON = "";
    try {
      responseJSON = new ObjectMapper().writeValueAsString("");
    } catch (JsonProcessingException e) {
      responseJSON = "";
    }
    return Response.status(HttpStatus.SC_BAD_REQUEST).entity(responseJSON).type(MediaType.APPLICATION_JSON).build();
  }
}
