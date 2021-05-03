package me.kolek.ecommerce.dsgw.aws.sqs;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class MessageProcessingResult {
  private final boolean retry;
}
