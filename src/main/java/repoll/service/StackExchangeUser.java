package repoll.service;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.Nullable;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import static repoll.service.ServiceUtil.StackExchangeResponseWrapper;

public class StackExchangeUser {
    public static final String STACKEXCHAGE_USER_API_URI = "https://api.stackexchange.com/2.1/users/";

    @Nullable
    public static StackExchangeUser loadById(int id) throws IOException {
        InputStream response = ClientBuilder.newClient().target(STACKEXCHAGE_USER_API_URI)
                .path(String.valueOf(id))
                .queryParam("site", "stackoverflow")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .acceptEncoding("utf-8")
                .get(InputStream.class);
        InputStreamReader decodedStream = new InputStreamReader(new GZIPInputStream(response));
        String json = ServiceUtil.consumeStream(decodedStream);
//        System.err.println(json);
        StackExchangeResponseWrapper<StackExchangeUser> wrapper = ServiceUtil.GSON.fromJson(json, ServiceUtil.WRAPPER_TYPE);
        return wrapper.hasItems() ? wrapper.getItems().get(0) : null;
    }

    /**
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
        System.out.println(loadById(1000000000));
    }

}
