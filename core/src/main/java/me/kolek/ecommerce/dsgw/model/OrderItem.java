package me.kolek.ecommerce.dsgw.model;

import java.time.OffsetDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
public class OrderItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;

  @Column(name = "line_number", nullable = false)
  private Integer lineNumber;

  @ManyToOne(optional = false)
  @JoinColumn(name = "catalog_item_id", nullable = false)
  private CatalogItem catalogItem;

  @Column(name = "quantity", nullable = false)
  private Integer quantity;

  @Column(name = "customization")
  private String customization;

  @Column(name = "expected_ship_date")
  private OffsetDateTime expectedShipDate;

  @Column(name = "expected_delivery_date")
  private OffsetDateTime expectedDeliveryDate;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private Order.Status status;

  @Column(name = "quantity_accepted")
  private Integer quantityAccepted;

  @Column(name = "quantity_rejected")
  private Integer quantityRejected;

  @ManyToOne
  @JoinColumn(name = "reject_code_id")
  private OrderCancelCode rejectCode;

  @Column(name = "reject_reason")
  private String rejectReason;

  @Column(name = "time_acknowledged")
  private OffsetDateTime timeAcknowledged;

  @Column(name = "quantity_cancelled")
  private Integer quantityCancelled;

  @ManyToOne
  @JoinColumn(name = "cancel_code_id")
  private OrderCancelCode cancelCode;

  @Column(name = "cancel_reason")
  private String cancelReason;

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
}
