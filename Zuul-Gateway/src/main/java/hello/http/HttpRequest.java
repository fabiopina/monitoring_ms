package hello.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpRequest {

    public static void post(String message) {
        HttpClient client = HttpClientBuilder.create().build();

        String logServer =  System.getenv("LOGSERVERADDRESS");
        HttpPost post = new HttpPost("http://"+logServer+"/log");

        String payload = "{\"message\": \""+message+"\"}";

        try {
            StringEntity postingString = new StringEntity(payload);
            post.setHeader("content-type", "application/json");
            post.setEntity(postingString);
            HttpResponse response = client.execute(post);

            // Print out the response message
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
