package pt.fabiopina.filters.post;

import com.netflix.client.IResponse;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.ZuulFilter;
import pt.fabiopina.entities.RawInfoEntity;
import pt.fabiopina.queue.RequestQueue;

import javax.servlet.http.HttpServletRequest;

public class InfoRequestFilter extends ZuulFilter {
    private RequestQueue queue;

    public InfoRequestFilter(RequestQueue q) {
        queue = q;
    }

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        long timeEnd = System.currentTimeMillis();
        queue.add(new RawInfoEntity(request.getRemoteAddr(), request.getMethod(), request.getRequestURL().toString(), ((IResponse) ctx.get("ribbonResponse")).getRequestedURI().toString().substring(7), request.getRemotePort(), ctx.getResponseStatusCode(), Long.parseLong(ctx.getZuulRequestHeaders().get("time-start")), timeEnd));
        return null;
    }
}
