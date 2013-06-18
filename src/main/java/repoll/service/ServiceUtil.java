package repoll.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;

public class ServiceUtil {
    public static final Gson GSON = buildGson();
    public static final Type WRAPPER_TYPE = new TypeToken<StackExchangeResponseWrapper<StackExchangeUser>>() {}.getType();

    private static Gson buildGson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setPrettyPrinting()
                .create();
    }

    public static String consumeStream(Reader reader) throws IOException {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[256];
        int nread;
        try {
            while ((nread = reader.read(buffer)) > 0) {
                builder.append(buffer, 0, nread);
            }
            return builder.toString();
        } finally {
            reader.close();
        }
    }

    /**
     * Auxiliary response holder for StackExchange services
     */

    public static class StackExchangeResponseWrapper<T> {
        private List<T> items;

        public boolean hasItems() {
            return !items.isEmpty();
        }

        public List<T> getItems() {
            return items;
        }
    }
}
