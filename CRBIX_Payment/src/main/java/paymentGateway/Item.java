package paymentGateway;

import java.io.Serializable;
import java.math.BigDecimal;

public class Item implements Serializable {
    private String id;
    private String name;
    private BigDecimal price;
    private String image; // relative path

    public Item() {}

    public Item(String id, String name, BigDecimal price, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}

