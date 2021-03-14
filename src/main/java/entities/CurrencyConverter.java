package entities;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class CurrencyConverter {
    private final Map<String, Function<Double, Double>> coefs;
    public CurrencyConverter () {
        // may update information in runtime
        coefs = new HashMap<>();
        coefs.put("ru", val -> 78.0 * val);
        coefs.put("us", val -> val);
        coefs.put("eu", val -> 0.84 * val);
    }

    double convert(String key, double val) {
        if (!coefs.containsKey(key.toLowerCase())) {
            return Double.MAX_VALUE;
        }
        return coefs.get(key.toLowerCase()).apply(val);
    }
}
