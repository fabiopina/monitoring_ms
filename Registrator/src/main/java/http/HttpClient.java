package http;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

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

    public static int put(String url) throws Exception {
        org.apache.http.client.HttpClient client = HttpClientBuilder.create().build();

        HttpPut put = new HttpPut(url);
        put.setHeader("Content-Type", "application/json");
        HttpResponse response = client.execute(put);
        return response.getStatusLine().getStatusCode();
    }

    public static int delete(String url) throws Exception {
        org.apache.http.client.HttpClient client = HttpClientBuilder.create().build();

        HttpDelete delete = new HttpDelete(url);
        delete.setHeader("Content-Type", "application/json");
        HttpResponse response = client.execute(delete);
        return response.getStatusLine().getStatusCode();
    }
}
