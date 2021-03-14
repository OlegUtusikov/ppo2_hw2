import entities.Product;
import entities.User;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.netty.protocol.http.server.HttpServer;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import io.reactivex.netty.protocol.http.server.ResponseContentWriter;
import rx.Observable;

public class Server {
    private final DB database;

    public Server(DB database) {
        this.database = database;
    }

    public void start() {
        HttpServer
                .newServer(Config.server_port)
                .start((req, resp) -> {
                    String[] paths = req.getDecodedPath().substring(1).split("/");
                    return switch (paths[0]) {
                        case ("regUser") -> paths.length == 3 ? processRegistrationUser(resp, paths[1], paths[2]) : badReq(resp, "Incorrect args count " + paths.length);
                        case ("Users") -> paths.length == 1 ? processGetUsers(resp) : badReq(resp, "Incorrect args count " + paths.length);
                        case ("User") -> paths.length == 2 ? processGetUser(resp, paths[1]) : badReq(resp, "Incorrect args count " + paths.length);
                        case ("addProduct") -> paths.length == 3 ? processAddProduct(resp, paths[1], Double.parseDouble(paths[2])) : badReq(resp, "Incorrect args count " + paths.length);
                        case ("Products") -> paths.length == 2 ? processGetUserProducts(resp, paths[1]) : badReq(resp, "Incorrect args count " + paths.length);
                        case ("Product") -> paths.length == 2 ? processGetProduct(resp, paths[1]) : badReq(resp, "Incorrect args count " + paths.length);
                        default -> notFound(resp, "Bad path: " + paths[0]);
                    };
                })
                .awaitShutdown();
    }

    private ResponseContentWriter<ByteBuf> notFound(HttpServerResponse<ByteBuf> rsp, String msg) {
        rsp.setStatus(HttpResponseStatus.NOT_FOUND);
        return setContent(rsp, msg);
    }

    private ResponseContentWriter<ByteBuf> badReq(HttpServerResponse<ByteBuf> rsp, String msg) {
        rsp.setStatus(HttpResponseStatus.BAD_REQUEST);
        return setContent(rsp, msg);
    }

    private ResponseContentWriter<ByteBuf> setContent(HttpServerResponse<ByteBuf> rsp, String msg) {
        return rsp.writeString(Observable.just(msg));
    }

    private ResponseContentWriter<ByteBuf> processRegistrationUser(HttpServerResponse<ByteBuf> rsp, String name, String currency) {
        User user = new User(name, currency);
        database.addUser(user);
        rsp.setStatus(HttpResponseStatus.ACCEPTED);
        return setContent(rsp, "User was added");
    }

    private ResponseContentWriter<ByteBuf> processGetUser(HttpServerResponse<ByteBuf> rsp, String userId) {
        rsp.setStatus(HttpResponseStatus.FOUND);
        return rsp.writeString(database.getUser(userId).map(user -> user.toString() + "\n"));
    }

    private ResponseContentWriter<ByteBuf> processGetUsers(HttpServerResponse<ByteBuf> rsp) {
        rsp.setStatus(HttpResponseStatus.FOUND);
        return rsp.writeString(database.getAllUsers().map(user -> user.toString() + "\n"));
    }

    private ResponseContentWriter<ByteBuf> processAddProduct(HttpServerResponse<ByteBuf> rsp, String title, double cost) {
        Product product = new Product(title, cost);
        database.addProduct(product);
        rsp.setStatus(HttpResponseStatus.ACCEPTED);
        return setContent(rsp, "Product was added");
    }

    private ResponseContentWriter<ByteBuf> processGetUserProducts(HttpServerResponse<ByteBuf> rsp, String userId) {
        Observable<Product> products = database.getAllProducts();
        Observable<User> user = database.getUser(userId);
        rsp.setStatus(HttpResponseStatus.FOUND);
        return rsp.writeString(user.map(User::getCurrency).flatMap(currency -> products.map(product -> product.toString(currency) + "\n")));
    }

    private ResponseContentWriter<ByteBuf> processGetProduct(HttpServerResponse<ByteBuf> rsp, String id) {
        rsp.setStatus(HttpResponseStatus.FOUND);
        System.out.println(id);
        return rsp.writeString(database.getProduct(id).map(product -> product.toString("en") + "\n"));
    }
}

