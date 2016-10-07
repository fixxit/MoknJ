package nl.fixx.asset.data.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.data.annotation.Id;

public class Asset {
    @Id
    private String id;

    private String name;
    private String description;
    private BigDecimal price;
    private Date purchaseDate;

    public Asset() {
    }

    public Asset(String name, String description, BigDecimal price, Date purchaseDate) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.purchaseDate = purchaseDate;
    }

    @Override
    public String toString() {
        return "Asset{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", purchaseDate=" + purchaseDate +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

}
