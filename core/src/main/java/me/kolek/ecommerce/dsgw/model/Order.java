package me.kolek.ecommerce.dsgw.model;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
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
@Table(name = "\"order\"")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false)
  private Long id;

  @Column(name = "external_id", nullable = false, length = 32)
  private String externalId;

  @Column(name = "customer_order_number", nullable = false, length = 32)
  private String customerOrderNumber;

  @ManyToOne
  @JoinColumn(name = "warehouse_id")
  private Warehouse warehouse;

  @Embedded
  private Contact contact;

  @Embedded
  private Address address;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  @OrderBy("lineNumber ASC")
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<OrderItem> items;

  @ManyToOne
  @JoinColumn(name = "service_level_id")
  private ServiceLevel serviceLevel;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private Status status;

  @ManyToOne
  @JoinColumn(name = "cancel_code_id")
  private OrderCancelCode cancelCode;

  @Column(name = "cancel_reason")
  private String cancelReason;

  @Column(name = "time_ordered", nullable = false)
  private OffsetDateTime timeOrdered;

  @Column(name = "time_released", nullable = false)
  private OffsetDateTime timeReleased;

  @Column(name = "time_cancelled")
  private OffsetDateTime timeCancelled;

  @CreationTimestamp
  @Column(name = "time_created", nullable = false)
  private OffsetDateTime timeCreated;

  @UpdateTimestamp
  @Column(name = "time_updated", nullable = false)
  private OffsetDateTime timeUpdated;

  @Version
  @Column(name = "record_version", nullable = false)
  private Short recordVersion;

  public void addItem(OrderItem item) {
    if (items == null) {
      items = new ArrayList<>();
    }
    item.setOrder(this);
    items.add(item);
  }

  public enum Status {
    NEW,
    ACKNOWLEDGED,
    SHIPPED_PARTIAL,
    SHIPPED,
    INVOICED,
    CANCELLED
  }
}
