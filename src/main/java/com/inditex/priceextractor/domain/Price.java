package com.inditex.priceextractor.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Currency;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(
        name = "prices",
        indexes = {
                @Index(name = "price_brand_id_and_product_id_index", columnList = "brand_id , product_id")
        }
)
public class Price {

    @Id()
    @Column(name = "price_list")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "brand_id", nullable = false)
    private long brandId;
    @Column(name = "start_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date startDate;
    @Column(name = "end_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date endDate;
    @Column(name = "product_id", nullable = false)
    private long productId;
    @Column(name = "priority", nullable = false)
    private int priority;
    @Column(name = "price", nullable = false)
    private double price;
    @Column(name = "curr", nullable = false)
    @NotNull
    private Currency curr;

    protected Price() {
    }

    public Price(
            long id,
            long brandId,
            Date startDate,
            Date endDate,
            long productId,
            int priority,
            double price,
            Currency curr
    ) {
        this.id = id;
        this.assertDateRangeIsValid(startDate,endDate);
        this.brandId = brandId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.productId = productId;
        this.priority = priority;
        this.price = price;
        this.curr = curr;
    }

    private void assertDateRangeIsValid(Date startDate, Date endDate) throws RuntimeException {
        if (!startDate.before(endDate)) {
            throw new RuntimeException("DomainError: startDate can not bee newer than endDate");
        }
    }

    public long getBrandId() {
        return brandId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public long getId() {
        return id;
    }

    public long getProductId() {
        return productId;
    }

    public int getPriority() {
        return priority;
    }

    public double getPrice() {
        return price;
    }

    public Currency getCurr() {
        return curr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price1 = (Price) o;
        return id == price1.id && brandId == price1.brandId && productId == price1.productId && priority == price1.priority && Double.compare(price1.price, price) == 0 && startDate.equals(price1.startDate) && endDate.equals(price1.endDate) && curr.equals(price1.curr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, brandId, startDate, endDate, productId, priority, price, curr);
    }
}
