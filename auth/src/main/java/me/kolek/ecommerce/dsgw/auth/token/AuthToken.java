package me.kolek.ecommerce.dsgw.auth.token;

import java.time.OffsetDateTime;
import java.util.Set;

public record AuthToken(String value, String type, OffsetDateTime expiration, Set<String> scopes) {
}
