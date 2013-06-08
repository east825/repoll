package repoll.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ServiceUtil {
    public static final Gson GSON = buildGson();

    private static Gson buildGson() {
        return new GsonBuilder()
                .setDateFormat("Y-m-d'T'H:M:SZ")
                .setPrettyPrinting()
                .create();
    }
}
