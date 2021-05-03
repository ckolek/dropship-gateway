package me.kolek.ecommerce.dsgw.internal.model.queue;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageAttributes {

  public static final String REQUEST_ID = "RequestID";
  public static final String RESPONSE_QUEUE_URL = "ResponseQueueURL";
}