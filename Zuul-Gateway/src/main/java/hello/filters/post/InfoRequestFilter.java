package hello.filters.post;

import com.netflix.client.IResponse;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.ZuulFilter;

import hello.queue.CtxInfoObject;
import hello.queue.RequestQueue;

import javax.servlet.http.HttpServletRequest;

public class InfoRequestFilter extends ZuulFilter {
    private RequestQueue queue;

    public InfoRequestFilter(RequestQueue q) {
        queue = q;
    }

    // returns a String that stands for the type of the filter---in this case, pre, or it could be route for a routing filter.
    @Override
    public String filterType() {
        return "post";
    }

    // gives the order in which this filter will be executed, relative to other filters.
    @Override
    public int filterOrder() {
        return 2;
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

        long timeEnd = System.currentTimeMillis();

        CtxInfoObject c = CtxInfoObject.constructObjectFromCtx(Long.parseLong(ctx.getZuulRequestHeaders().get("time-start")), timeEnd, request.getRemoteAddr(), request.getMethod(), request.getRequestURL().toString(), ((IResponse) ctx.get("ribbonResponse")).getRequestedURI().toString().substring(7), request.getRemotePort());

        queue.add(c);

        return null;
    }
}
