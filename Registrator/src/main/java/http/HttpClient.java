package http;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class HttpClient {

    public static int post(String url, String payload) throws Exception {

        org.apache.http.client.HttpClient client = HttpClientBuilder.create().build();

        HttpPost post = new HttpPost(url);

        StringEntity postingString = new StringEntity(payload);
        post.setHeader("Content-Type", "application/json");
        post.setEntity(postingString);
        HttpResponse response = client.execute(post);
        return response.getStatusLine().getStatusCode();
    }
}
