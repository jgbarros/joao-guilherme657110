package br.gov.mt.seplag.seletivo.joao_guilherme657110_api.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitFilter implements Filter {

    private final Map<String, UserRequestInfo> requestCounts = new ConcurrentHashMap<>();
    private static final int MAX_REQUESTS_PER_MINUTE = 100;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Ignorar requisições OPTIONS (CORS preflight)
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            String username = authentication.getName();
            UserRequestInfo info = requestCounts.computeIfAbsent(username, k -> new UserRequestInfo());
            
            synchronized (info) {
                long currentTime = System.currentTimeMillis();
                
                // Reseta o contador se passou mais de 1 minuto
                if (currentTime - info.lastResetTime > 60000) {
                    info.requestCount.set(0);
                    info.lastResetTime = currentTime;
                }
                
                if (info.requestCount.incrementAndGet() > MAX_REQUESTS_PER_MINUTE) {
                    httpResponse.setStatus(429); // Too Many Requests
                    httpResponse.getWriter().write("Limite de requisicoes excedido. Tente novamente em breve.");
                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }

    private static class UserRequestInfo {
        AtomicInteger requestCount = new AtomicInteger(0);
        long lastResetTime = System.currentTimeMillis();
    }
}
