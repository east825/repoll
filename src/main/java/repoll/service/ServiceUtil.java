package repoll.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class ServiceUtil {
    public static final Gson GSON = buildGson();

    private static Gson buildGson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setPrettyPrinting()
                .create();
    }

    public static class StackExchangeResponseWrapper<T> {
        private List<T> items;

        public List<T> getItems() {
            return items;
        }
    }
}
