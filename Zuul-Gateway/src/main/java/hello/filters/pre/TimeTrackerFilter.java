package hello.filters.pre;

import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.ZuulFilter;

public class TimeTrackerFilter extends ZuulFilter {

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

        ctx.addZuulRequestHeader("time-start", "" + System.currentTimeMillis());

        return null;
    }
}
