package entities;

import org.bson.Document;
import org.bson.types.ObjectId;

public class Product {
    private final ObjectId id;
    private String title;
    private double cost;

    public Product(String title, double cost) {
        this.id = new ObjectId();
        this.title = title;
        this.cost = cost;
    }

    public Product(Document document) {
        this.id = document.getObjectId("_id");
        this.title = document.getString("title");
        this.cost = document.getDouble("cost");
    }

    public ObjectId getId () {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String toString(String currency) {
        CurrencyConverter converter = new CurrencyConverter();
        return "Product {id: " + id.toString() + ", title: " + title + ", cost: " + converter.convert(currency, cost) + "}";
    }
}
