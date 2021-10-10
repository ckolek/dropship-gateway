package me.kolek.ecommerce.dsgw.api.graphql;

import graphql.ErrorType;
import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.kickstart.execution.error.DefaultGraphQLErrorHandler;
import graphql.kickstart.execution.error.GraphQLErrorHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import me.kolek.ecommerce.dsgw.auth.AuthException;
import org.springframework.stereotype.Component;

@Component
public class GraphQLExceptionHandler extends DefaultGraphQLErrorHandler implements
    GraphQLErrorHandler {

  @Override
  public List<GraphQLError> processErrors(List<GraphQLError> errors) {
    errors = errors.stream().map(error -> {
      if (error instanceof ExceptionWhileDataFetching ewdf) {
        return handleExceptionWhileDataFetching(ewdf, ewdf.getException());
      }
      return error;
    }).collect(Collectors.toList());
    return super.processErrors(errors);
  }

  private static GraphQLError handleExceptionWhileDataFetching(ExceptionWhileDataFetching exception,
      Throwable cause) {
    if (cause instanceof AuthException ae) {
      return handleAuthException(exception, ae);
    }
    return exception;
  }

  private static GraphQLError handleAuthException(GraphQLError error, AuthException exception) {
    Map<String, Object> extensions = new HashMap<>();
    extensions.put("code", exception.getType().name());
    if (exception.getScope() != null) {
      extensions.put("scope", exception.getScope().toString());
    }

    return GraphqlErrorBuilder.newError()
        .errorType(ErrorType.ExecutionAborted)
        .message(exception.getMessage())
        .locations(error.getLocations())
        .path(error.getPath())
        .extensions(extensions)
        .build();
  }
}
