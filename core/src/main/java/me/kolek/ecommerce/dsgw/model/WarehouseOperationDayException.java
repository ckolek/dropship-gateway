package me.kolek.ecommerce.dsgw.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "warehouse_oper_day_except")
public class WarehouseOperationDayException {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "warehouse_id", nullable = false, updatable = false)
  private Warehouse warehouse;

  @Column(name = "date", nullable = false)
  private LocalDate date;

  @Column(name = "open", nullable = false)
  private Boolean open;

  @Column(name = "openTime")
  private LocalTime openTime;

  @Column(name = "closeTime")
  private LocalTime closeTime;

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
