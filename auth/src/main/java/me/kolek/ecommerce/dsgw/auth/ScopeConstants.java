package me.kolek.ecommerce.dsgw.auth;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ScopeConstants {

  public static final String READ = "read";
  public static final String WRITE = "write";
  public static final String ORG = "org";
  public static final String ORG_ID = "{orgId}";
  public static final String ORG_TYPE = "{orgType}";
  public static final String USER = "user";
  public static final String USER_ID = "{userId}";
}
