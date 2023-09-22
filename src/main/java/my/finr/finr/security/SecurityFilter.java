package my.finr.finr.security;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import my.finr.finr.exceptionHandling.UserNotFoundException;
import my.finr.finr.model.User;
import my.finr.finr.service.JWTService;
import my.finr.finr.service.UserService;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    JWTService jwtService;

    @Autowired
    UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Optional<String> tokenByRequest = getTokenByRequest(request);
        tokenByRequest.ifPresent((token) -> {
            try {
                String nickname = jwtService.validateToken(token);
                User user = userService.findByNickname(nickname);
                var authentication = new UsernamePasswordAuthenticationToken(user.getNickname(), user.getPassword(), user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (UserNotFoundException e) {
                e.printStackTrace();
            }
        });
        filterChain.doFilter(request, response);

    }

    private Optional<String> getTokenByRequest(HttpServletRequest request) {
        Optional<String> authHeader = Optional.ofNullable(request.getHeader("Authorization"));
        if (authHeader.isEmpty())
            return Optional.ofNullable(null);
        return Optional.ofNullable(authHeader.get().replace("Bearer ", ""));
    }
}
