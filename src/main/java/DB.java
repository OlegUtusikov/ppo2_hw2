import com.mongodb.rx.client.MongoClients;
import com.mongodb.rx.client.MongoDatabase;
import entities.Product;
import entities.User;
import org.bson.Document;
import org.bson.types.ObjectId;
import rx.Observable;

import static com.mongodb.client.model.Filters.eq;


public class DB {
    private final MongoDatabase database;

    public DB() {
        database = MongoClients.create().getDatabase(Config.database_name);
    }

    public void addProduct(Product product) {
        Document productRaw = new Document().append("title", product.getTitle()).append("cost", product.getCost());
        database.getCollection(Config.products_table_name).insertOne(productRaw).subscribe(success -> System.out.println("Product added"));
    }

    public Observable<Product> getProduct(String id) {
        return database.getCollection(Config.products_table_name).find(eq("_id", new ObjectId(id))).toObservable().map(Product::new);
    }

    public Observable<Product> getAllProducts() {
        return database.getCollection(Config.products_table_name).find().toObservable().map(Product::new);
    }

    public Observable<User> getUser(String id) {
        return database.getCollection(Config.users_table_name).find(eq("_id", new ObjectId(id))).toObservable().map(User::new);
    }

    public Observable<User> getAllUsers() {
        return database.getCollection(Config.users_table_name).find().toObservable().map(User::new);
    }

    public void addUser(User user) {
        Document userRaw = new Document().append("name", user.getName()).append("currency", user.getCurrency());
        database.getCollection(Config.users_table_name).insertOne(userRaw).subscribe(success -> System.out.println("User added"));
    }
}
