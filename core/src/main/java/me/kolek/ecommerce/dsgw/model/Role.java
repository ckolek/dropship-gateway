package me.kolek.ecommerce.dsgw.model;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "client_role")
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "owner_id", nullable = false, updatable = false)
  private Organization owner;

  @Column(name = "name", nullable = false, updatable = false, length = 32)
  private String name;

  @ManyToMany
  @JoinTable(
      name = "client_role_scopes",
      joinColumns = @JoinColumn(name = "role_id", nullable = false, updatable = false),
      inverseJoinColumns = @JoinColumn(name = "scope_id", nullable = false, updatable = false))
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Set<Scope> scopes;

  @CreationTimestamp
  @Column(name = "time_created", nullable = false)
  private OffsetDateTime timeCreated;

  @UpdateTimestamp
  @Column(name = "time_updated", nullable = false)
  private OffsetDateTime timeUpdated;

  @Version
  @Column(name = "record_version", nullable = false)
  private Short recordVersion;

  public void addScope(Scope scope) {
    if (scopes == null) {
      scopes = new HashSet<>();
    }
    scopes.add(scope);
  }

  public void removeScope(Scope scope) {
    if (scopes != null) {
      scopes.remove(scope);
    }
  }
}
