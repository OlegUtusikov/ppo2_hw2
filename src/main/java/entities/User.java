package entities;

import org.bson.Document;
import org.bson.types.ObjectId;

public class User {
    private final ObjectId id;
    private String name;
    private String currency;

    public User(String name, String currency) {
        this.id = ObjectId.get();
        this.name = name;
        this.currency = currency;
    }

    public User(Document document) {
        this.id = document.getObjectId("_id");
        this.name = document.getString("name");
        this.currency = document.getString("currency");
    }

    public ObjectId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public  void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String toString() {
        return "User {id: " + id.toString() + ", name: " + name + ", currency: " + currency + "}";
    }
}
