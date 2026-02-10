package com.arriaga.invex.employeeservice.logging;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

  private static final Logger requestLogger = LoggerFactory.getLogger(RequestLoggingFilter.class);
  private static final String MASKED_VALUE = "[masked]";

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    Map<String, String> headers = extractHeaders(request);
    requestLogger.info("Incoming request method={} path={} headers={}",
        request.getMethod(),
        request.getRequestURI(),
        headers);
    filterChain.doFilter(request, response);
  }

  private Map<String, String> extractHeaders(HttpServletRequest request) {
    Map<String, String> headers = new LinkedHashMap<>();
    Enumeration<String> names = request.getHeaderNames();
    if (names == null) {
      return Collections.emptyMap();
    }
    while (names.hasMoreElements()) {
      String name = names.nextElement();
      String value = request.getHeader(name);
      headers.put(name, sanitizeHeader(name, value));
    }
    return headers;
  }

  private String sanitizeHeader(String name, String value) {
    if (value == null) {
      return null;
    }
    String lower = name.toLowerCase();
    if (lower.contains("authorization") || lower.contains("cookie") || lower.contains("set-cookie")) {
      return MASKED_VALUE;
    }
    return value;
  }
}
