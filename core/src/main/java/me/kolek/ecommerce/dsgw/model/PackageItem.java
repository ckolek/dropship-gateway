package me.kolek.ecommerce.dsgw.model;

import java.time.OffsetDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
public class PackageItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false)
  @Getter
  @Setter
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "package_id", nullable = false)
  private Package _package;

  @ManyToOne(optional = false)
  @JoinColumn(name = "order_item_id", nullable = false)
  @Getter
  @Setter
  private OrderItem orderItem;

  @Column(name = "quantity", nullable = false)
  @Getter
  @Setter
  private Integer quantity;

  @CreationTimestamp
  @Column(name = "time_created", nullable = false)
  @Getter
  @Setter
  private OffsetDateTime timeCreated;

  @UpdateTimestamp
  @Column(name = "time_updated", nullable = false)
  @Getter
  @Setter
  private OffsetDateTime timeUpdated;

  @Version
  @Column(name = "record_version", nullable = false)
  @Getter
  @Setter
  private Short recordVersion;

  public Package getPackage() {
    return _package;
  }

  public void setPackage(Package _package) {
    this._package = _package;
  }
}
