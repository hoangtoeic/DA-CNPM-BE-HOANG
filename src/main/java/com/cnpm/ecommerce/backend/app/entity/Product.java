package com.cnpm.ecommerce.backend.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "product")
@JsonIgnoreProperties({"feedbacks", "thumbnailArr", "category", "brandEntity"})
public class Product extends BaseEntity{

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "brand")
    private String brand;

    @Column(name = "short_description" ,columnDefinition = "TEXT")
    private String shortDescription;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "unit_in_stock")
    private Integer unitInStock;

    @Column(name = "thumbnail", length = 100000)
    @Lob
    private byte[] thumbnailArr;

    @Transient
    private String thumbnail;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    @JsonIgnoreProperties("products")
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_id")
    @JsonIgnoreProperties("products")
    private Brand brandEntity;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("product")
    Set<Feedback> feedbacks;

    @Transient
    @JsonProperty(value = "categoryId")
    private Long categoryIds;

    @Column(name = "discount")
    private Integer discount;

    @Column(name = "rating_average")
    private BigDecimal ratingAverage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public Integer getUnitInStock() {
        return unitInStock;
    }

    public void setUnitInStock(Integer unitInStock) {
        this.unitInStock = unitInStock;
    }

    public byte[] getThumbnailArr() {
        return thumbnailArr;
    }

    public void setThumbnailArr(byte[] thumbnailArr) {
        this.thumbnailArr = thumbnailArr;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Product() {
        super();
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Brand getBrandEntity() { return brandEntity; }

    public void setBrandEntity(Brand brandEntity) { this.brandEntity = brandEntity; }

    public Set<Feedback> getFeedbacks() { return feedbacks; }

    public void setFeedbacks(Set<Feedback> feedbacks) { this.feedbacks = feedbacks; }

    public Long getCategoryIds() { return categoryIds; }

    public void setCategoryIds(Long categoryIds) { this.categoryIds = categoryIds; }

    public Integer getDiscount() { return discount; }

    public void setDiscount(Integer discount) { this.discount = discount; }

    public BigDecimal getRatingAverage() { return ratingAverage; }

    public void setRatingAverage(BigDecimal ratingAverage) { this.ratingAverage = ratingAverage; }
}
