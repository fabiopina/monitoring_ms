package hello.filters.pre;

import javax.servlet.http.HttpServletRequest;

import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.ZuulFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hello.http.RequestQueue;

import java.sql.Timestamp;

public class LogIncomingRequest extends ZuulFilter {
    private RequestQueue queue;
    private static Logger log = LoggerFactory.getLogger(LogIncomingRequest.class);

    public LogIncomingRequest(RequestQueue q) {
        queue = q;
    }

    // returns a String that stands for the type of the filter---in this case, pre, or it could be route for a routing filter.
    @Override
    public String filterType() {
        return "pre";
    }

    // gives the order in which this filter will be executed, relative to other filters.
    @Override
    public int filterOrder() {
        return 1;
    }

    // contains the logic that determines when to execute this filter (this particular filter will always be executed).
    @Override
    public boolean shouldFilter() {
        return true;
    }

    // contains the functionality of the filter.
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        // 1st -> Timestamp
        // 2nd -> Type
        // 3rd -> Method
        // 4th -> Client ip address
        // 5th -> Client port
        // 6th -> URL used by client
        // 7th -> Microservice called
        // 8th -> Microservice hostname
        // 9th -> Microservice ip address
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String message = String.format("%s %s %s %s %s %s %s %s %s", timestamp.toString(), "INCOMING", request.getMethod(), request.getRemoteAddr(), request.getRemotePort(), request.getRequestURL().toString(), null, null, null);

        log.info(message);
        queue.add(message);

        return null;
    }
}
