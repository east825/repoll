package repoll.server.rest;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.Nullable;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import static repoll.server.rest.ServiceUtil.StackExchangeResponseWrapper;

public class StackExchangeUser {
    private static final String STACKEXCHAGE_USER_API_URI = "https://api.stackexchange.com/2.1/users/";
    private static Logger LOG = Logger.getLogger(StackExchangeUser.class.getName());

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
    private URL profileImageLink;
    @SerializedName("user_id")
    private int id;
    private int reputation;
    private int age;
    @SerializedName("accept_rate")
    private int acceptRate;

    @Override
    public String toString() {
        return String.format("StackExchangeUser(id=%d, name='%s')", id, displayName);
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public String getDisplayName() {
        return displayName;
    }

    public URL getProfileImageLink() {
        return profileImageLink;
    }

    public int getId() {
        return id;
    }

    public int getReputation() {
        return reputation;
    }

    public int getAge() {
        return age;
    }

    public int getAcceptRate() {
        return acceptRate;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(loadById(1013522));
    }

}
