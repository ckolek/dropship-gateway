package me.kolek.ecommerce.dsgw.events.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.events.SNSEvent.SNSRecord;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.lang.reflect.Type;
import me.kolek.ecommerce.dsgw.events.handler.util.LambdaLoggerUtil;

public abstract class SNSEventRequestHandler<T> implements RequestHandler<SNSEvent, String> {
  protected final ObjectMapper objectMapper;
  private final TypeReference<T> type;

  public SNSEventRequestHandler(ObjectMapper objectMapper, TypeReference<T> type) {
    this.objectMapper = objectMapper;
    this.type = type;
  }

  public SNSEventRequestHandler(ObjectMapper objectMapper, Class<T> type) {
    this(objectMapper, new TypeReference<T>() {
      @Override
      public Type getType() {
        return type;
      }
    });
  }

  @Override
  public String handleRequest(SNSEvent event, Context context) {
    for (var record : event.getRecords()) {
      try {
        handleRecord(record, context);
      } catch (IOException e) {
        LambdaLoggerUtil.printStackTraceMessage(context.getLogger(),
            "failed to handle record: message_id=" + record.getSNS().getMessageId(), e);
        return "500 SERVER ERROR";
      }
    }

    return "200 OK";
  }

  private void handleRecord(SNSRecord record, Context context) throws IOException {
    context.getLogger().log("handling record: message_id=" + record.getSNS().getMessageId() + "\n");

    handleRecord(objectMapper.readValue(record.getSNS().getMessage(), type), context);
  }

  protected abstract void handleRecord(T record, Context context) throws IOException;
}
