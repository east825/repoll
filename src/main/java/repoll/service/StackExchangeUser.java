package repoll.service;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.zip.GZIPInputStream;

import static repoll.service.ServiceUtil.StackExchangeResponseWrapper;

public class StackExchangeUser {
    public static final String STACKEXCHAGE_USER_API_URI = "https://api.stackexchange.com/2.1/users/";
    public static final Type WRAPPER_TYPE = new TypeToken<StackExchangeResponseWrapper<StackExchangeUser>>() {}.getType();

    public static StackExchangeUser loadById(int id) throws IOException {
        InputStream response = ClientBuilder.newClient().target(STACKEXCHAGE_USER_API_URI)
                .path(String.valueOf(id))
                .queryParam("site", "stackoverflow")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .acceptEncoding("utf-8")
                .get(InputStream.class);
        InputStreamReader decodedStream = new InputStreamReader(new GZIPInputStream(response));
        String json = consumeStream(decodedStream);
        System.err.println(json);
        StackExchangeResponseWrapper<StackExchangeUser> wrapper = ServiceUtil.GSON.fromJson(json, WRAPPER_TYPE);
        return wrapper.getItems().get(0);
    }

    /*
     * Serialization constructor
     */
    public StackExchangeUser() {
        // empty
    }

    @SerializedName("about_me")
    private String additionalInfo;
    @SerializedName("display_name")
    private String displayName;
    @SerializedName("profile_image")
    private String profileImageLink;
    @SerializedName("user_id")
    private int id;
    private int reputation, age;

    @Override
    public String toString() {
        return String.format("StackExchangeUser(id=%d, name='%s')", id, displayName);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(loadById(1013522));
    }

    private static String consumeStream(Reader reader) throws IOException {
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
}
