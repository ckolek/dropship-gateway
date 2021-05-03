package me.kolek.ecommerce.dsgw.api.http;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import me.kolek.ecommerce.dsgw.context.RequestContext;

public class RequestContextInitializingFilter implements Filter {

  private static final String HEADER_NAME = "X-Request-ID";

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    String requestId = null;
    if (request instanceof HttpServletRequest) {
      var httpRequest = (HttpServletRequest) request;
      requestId = httpRequest.getHeader(HEADER_NAME);
    }
    try (var requestContext = RequestContext.initialize(requestId)) {
      chain.doFilter(request, response);
    }
  }
}
