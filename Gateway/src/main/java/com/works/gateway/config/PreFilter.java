package com.works.gateway.config;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

public class PreFilter extends ZuulFilter {

  @Override
  public String filterType() {
    return "pre";
  }

  @Override
  public int filterOrder() {
    return 10000;
  }

  @Override
  public boolean shouldFilter() {
    return true;
  }


  @Override
  public Object run() {
    RequestContext ctx = RequestContext.getCurrentContext();
    HttpServletRequest request = ctx.getRequest();

    System.out.println("Request Method : " + request.getMethod() + " Request URL : " + request.getRequestURL().toString());

    RequestContext context = RequestContext.getCurrentContext();

    @SuppressWarnings("unchecked") Set<String> ignoredHeaders = (Set<String>) context.get("ignoredHeaders");
    ignoredHeaders.remove("authorization");

    return null;
  }

}