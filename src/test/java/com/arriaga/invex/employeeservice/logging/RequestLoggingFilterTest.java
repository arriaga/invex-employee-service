package com.arriaga.invex.employeeservice.logging;

import static org.assertj.core.api.Assertions.assertThat;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class RequestLoggingFilterTest {

  private RequestLoggingFilter filter;
  private Logger logger;
  private ListAppender<ILoggingEvent> appender;

  @BeforeEach
  void setUp() {
    filter = new RequestLoggingFilter();
    logger = (Logger) LoggerFactory.getLogger(RequestLoggingFilter.class);
    appender = new ListAppender<>();
    appender.start();
    logger.addAppender(appender);
  }

  @AfterEach
  void tearDown() {
    logger.detachAppender(appender);
    appender.stop();
  }

  @Test
  void logsRequestHeadersWithMasking() throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/employees");
    request.addHeader("Authorization", "Bearer token");
    request.addHeader("X-Request-Id", "req-1");
    request.addHeader("Cookie", "session=abc");
    MockHttpServletResponse response = new MockHttpServletResponse();
    AtomicBoolean chainCalled = new AtomicBoolean(false);
    FilterChain chain = (req, res) -> chainCalled.set(true);

    filter.doFilter(request, response, chain);

    assertThat(chainCalled).isTrue();
    assertThat(appender.list).hasSize(1);
    String message = appender.list.get(0).getFormattedMessage();
    assertThat(message)
        .contains("method=GET")
        .contains("path=/employees")
        .contains("Authorization=[masked]")
        .contains("Cookie=[masked]")
        .contains("X-Request-Id=req-1");
  }
}
