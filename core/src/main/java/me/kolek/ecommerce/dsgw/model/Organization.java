package me.kolek.ecommerce.dsgw.model;

import java.time.OffsetDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "client_organization")
public class Organization {

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  private Long id;

  @MapsId
  @OneToOne
  @JoinColumn(name = "id", nullable = false, updatable = false)
  private ClientCredentials credentials;

  @Column(name = "name", nullable = false, updatable = false, length = 64)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false, updatable = false)
  private Type type;

  @CreationTimestamp
  @Column(name = "time_created", nullable = false)
  private OffsetDateTime timeCreated;

  @UpdateTimestamp
  @Column(name = "time_updated", nullable = false)
  private OffsetDateTime timeUpdated;

  @Version
  @Column(name = "record_version", nullable = false)
  private Short recordVersion;

  public enum Type {
    ADMIN,
    RETAILER,
    SUPPLIER
  }
}
