package hello.hello_spring.domain.jwt;

import hello.hello_spring.domain.jwt.token.TokenProvider;
import hello.hello_spring.domain.jwt.token.TokenStatus;
import hello.hello_spring.dto.TokenValidationResult;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.regex.Pattern;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final String AUTORIZATION_HEADER = "Autorization";
    private static final String BEARER_REGEX = "Bearer ([a-zA-Z0-9_\\-+/=]+)\\.([a-zA-Z0-9_\\-+/=]+)\\.([a-zA-Z0-9_.\\-+/=]*)";
    private static final Pattern BEARER_PATTERN = Pattern.compile(BEARER_REGEX);
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = resolveToken(request);

        if (!StringUtils.hasText(token)) {
            handleMissingToken(request, response, filterChain);
            return;
        }

        TokenValidationResult result = tokenProvider.validateToken(token);

        if (!result.isValid()) {
            handleWrongToken(request, response, filterChain, result);
            return;
        }

        handleValidToken(token, result.getClaims());
        filterChain.doFilter(request, response);
    }

    private void handleValidToken(String token, Claims claims) {
        Authentication authentication = tokenProvider.getAuthentication(token, claims);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("AUTH SUCCESS : {},", authentication.getName());

    }

    private void handleMissingToken(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws IOException, ServletException {
        request.setAttribute("result", new TokenValidationResult(TokenStatus.NO_AUTH_HEADER, null, null, null));
        filterChain.doFilter(request, response);

    }

    private void handleWrongToken(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain, TokenValidationResult tokenValidationResult)
            throws IOException, ServletException {
        request.setAttribute("result", tokenValidationResult);
        filterChain.doFilter(request, response);

    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTORIZATION_HEADER);
        if (bearerToken != null && BEARER_PATTERN.matcher(bearerToken).matches()) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
