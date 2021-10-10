package me.kolek.ecommerce.dsgw.events.handler.util;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class LambdaLoggerUtil {
  public static void printStackTraceMessage(LambdaLogger logger, String message, Throwable e) {
    ByteArrayOutputStream stream = new ByteArrayOutputStream();

    PrintStream printStream = new PrintStream(stream);
    printStream.print(message);
    printStream.print(": ");
    e.printStackTrace(printStream);
    printStream.print("\n");

    logger.log(stream.toByteArray());
  }
}
