package me.kolek.ecommerce.dsgw.model;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ClientCredentials {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false)
  private Long id;

  @NaturalId
  @Column(name = "client_id", nullable = false, updatable = false)
  private String clientId;

  @Column(name = "client_secret", nullable = false)
  private byte[] clientSecret;

  @Column(name = "client_type", nullable = false, updatable = false)
  @Enumerated(EnumType.STRING)
  private ClientType clientType;

  @ManyToMany
  @JoinTable(
      name = "client_roles",
      joinColumns = @JoinColumn(name = "client_id", nullable = false, updatable = false),
      inverseJoinColumns = @JoinColumn(name = "role_id", nullable = false, updatable = false))
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Set<Role> roles;

  @CreationTimestamp
  @Column(name = "time_created", nullable = false)
  private OffsetDateTime timeCreated;

  @UpdateTimestamp
  @Column(name = "time_updated", nullable = false)
  private OffsetDateTime timeUpdated;

  @Version
  @Column(name = "record_version", nullable = false)
  private Short recordVersion;

  public void addRole(Role role) {
    if (roles == null) {
      roles = new HashSet<>();
    }
    roles.add(role);
  }

  public void removeRole(Role role) {
    if (roles != null) {
      roles.remove(role);
    }
  }

  public enum ClientType {
    USER,
    ORGANIZATION,
    SERVICE
  }
}
