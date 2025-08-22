package wid.bmsbackend.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ReqResLoggingFilter extends OncePerRequestFilter {
    private static final String REQUEST_ID = "request_id";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestId = UUID.randomUUID().toString();
        MDC.put(REQUEST_ID, requestId);
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        filterChain.doFilter(requestWrapper, responseWrapper);
        stopWatch.stop();

        log.info(HttpLogMessage.createInstance(requestWrapper, responseWrapper, stopWatch).toString());

        responseWrapper.copyBodyToResponse();

        MDC.remove(REQUEST_ID);
    }

    record HttpLogMessage(
            String method,
            String uri,
            int status,
            long duration,
            String clientIp,
            Map<String, String[]> parameterMap
    ) {
        public static HttpLogMessage createInstance(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, StopWatch stopWatch) {
            return new HttpLogMessage(
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    stopWatch.getTotalTimeMillis(),
                    getClientIP(request),
                    request.getParameterMap()
            );
        }
    }

    static String getClientIP(HttpServletRequest request) {
        for (String header : headers) {
            String clientIp = request.getHeader(header);
            if (clientIp != null) {
                return getClientIPWithHeader(header, clientIp);
            }
        }
        return "Unknown";
    }

    static List<String> headers = List.of("X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR");

    static String getClientIPWithHeader(String header, String ip) {
        return header + " : " + ip;
    }
}
