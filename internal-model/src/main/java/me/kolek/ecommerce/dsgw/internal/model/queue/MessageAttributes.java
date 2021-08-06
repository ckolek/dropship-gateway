package me.kolek.ecommerce.dsgw.internal.model.queue;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageAttributes {

  public static final String EVENT_TYPE = "EventType";
  public static final String EVENT_SUB_TYPE = "EventSubType";
  public static final String ORDER_ID = "OrderID";
  public static final String REQUEST_ID = "RequestID";
  public static final String RESPONSE_QUEUE_URL = "ResponseQueueURL";
}
