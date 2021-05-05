package me.kolek.ecommerce.dsgw.model;

import java.time.OffsetDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CatalogItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "catalog_id", nullable = false)
  private Catalog catalog;

  @ManyToOne
  @JoinColumn(name = "parent_item_id")
  private CatalogItem parentItem;

  @OneToMany(mappedBy = "parentItem", cascade = CascadeType.ALL)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<CatalogItem> childItems;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "short_description")
  private String shortDescription;

  @Column(name = "long_description")
  private String longDescription;

  @Column(name = "sku", nullable = false)
  private String sku;

  @Column(name = "gtin")
  private String gtin;

  @Column(name = "upc")
  private String upc;

  @Column(name = "ean")
  private String ean;

  @Column(name = "isbn")
  private String isbn;

  @Column(name = "mpn")
  private String mpn;

  @Column(name = "manufacturer")
  private String manufacturer;

  @Column(name = "brand")
  private String brand;

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
