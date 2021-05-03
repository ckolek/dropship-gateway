package me.kolek.ecommerce.dsgw.aws.sqs;

import com.amazonaws.services.sqs.model.Message;

public interface MessageProcessor {
  String getQueueLabel();

  MessageProcessingResult processMessage(Message message) throws Exception;
}
