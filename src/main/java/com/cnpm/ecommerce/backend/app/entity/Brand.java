package com.cnpm.ecommerce.backend.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name= "brand", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
@JsonIgnoreProperties({"products", "thumbnailArr"})
public class Brand extends BaseEntity{

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "thumbnail", length = 100000)
    @Lob
    private byte[] thumbnailArr;

    @Transient
    private String thumbnail;

    @OneToMany(mappedBy = "brandEntity", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("brandEntity")
    private Set<Product> products = new HashSet<>();

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getThumbnail() { return thumbnail; }

    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }

    public byte[] getThumbnailArr() { return thumbnailArr; }

    public void setThumbnailArr(byte[] thumbnailArr) { this.thumbnailArr = thumbnailArr; }

    public Set<Product> getProducts() { return products; }

    public void setProducts(Set<Product> products) { this.products = products; }

    public Brand() {
        super();
    }

}
