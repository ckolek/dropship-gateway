package me.kolek.ecommerce.dsgw.model;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
public class Package {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false)
  private Long id;

  @Column(name = "external_id", nullable = false, length = 32)
  private String externalId;

  @ManyToOne
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;

  @ManyToOne
  @JoinColumn(name = "warehouse_id", nullable = false)
  private Warehouse warehouse;

  @Embedded
  @AttributeOverrides({@AttributeOverride(name = "name", column = @Column(name = "sender_name")),
      @AttributeOverride(name = "phone", column = @Column(name = "sender_phone")),
      @AttributeOverride(name = "email", column = @Column(name = "sender_email")),})
  private Contact senderContact;

  @Embedded
  @AttributeOverrides({@AttributeOverride(name = "line1", column = @Column(name = "sender_line1")),
      @AttributeOverride(name = "line2", column = @Column(name = "sender_line2")),
      @AttributeOverride(name = "line3", column = @Column(name = "sender_line3")),
      @AttributeOverride(name = "city", column = @Column(name = "sender_city")),
      @AttributeOverride(name = "state", column = @Column(name = "sender_state")),
      @AttributeOverride(name = "province", column = @Column(name = "sender_province")),
      @AttributeOverride(name = "postalCode", column = @Column(name = "sender_postal_code")),
      @AttributeOverride(name = "country", column = @Column(name = "sender_country"))})
  private Address senderAddress;

  @Embedded
  @AttributeOverrides({@AttributeOverride(name = "name", column = @Column(name = "recipient_name")),
      @AttributeOverride(name = "phone", column = @Column(name = "recipient_phone")),
      @AttributeOverride(name = "email", column = @Column(name = "recipient_email")),})
  private Contact recipientContact;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "line1", column = @Column(name = "recipient_line1")),
      @AttributeOverride(name = "line2", column = @Column(name = "recipient_line2")),
      @AttributeOverride(name = "line3", column = @Column(name = "recipient_line3")),
      @AttributeOverride(name = "city", column = @Column(name = "recipient_city")),
      @AttributeOverride(name = "state", column = @Column(name = "recipient_state")),
      @AttributeOverride(name = "province", column = @Column(name = "recipient_province")),
      @AttributeOverride(name = "postalCode", column = @Column(name = "recipient_postal_code")),
      @AttributeOverride(name = "country", column = @Column(name = "recipient_country"))})
  private Address recipientAddress;

  @ManyToOne
  @JoinColumn(name = "service_level_id", nullable = false)
  private ServiceLevel serviceLevel;

  @Column(name = "tracking_number", nullable = false, length = 64)
  private String trackingNumber;

  @OneToMany(mappedBy = "_package")
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<PackageItem> items;

  @Column(name = "time_shipped", nullable = false)
  private OffsetDateTime timeShipped;

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
