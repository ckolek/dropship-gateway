package me.kolek.ecommerce.dsgw.api.http;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpUtils {

  public static void respond(HttpServletResponse response, int status, String message,
      Object... args) throws IOException {
    response.setStatus(status);
    response.getWriter().println(String.format(message, args));
    response.getWriter().flush();
  }
}
