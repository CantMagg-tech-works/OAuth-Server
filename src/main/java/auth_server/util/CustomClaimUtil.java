package auth_server.util;

import auth_server.enums.AuthError;
import auth_server.model.EcUserModel;
import auth_server.repository.EcUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomClaimUtil implements OAuth2TokenCustomizer<JwtEncodingContext> {

  private final EcUserRepository ecUserRepository;


  @Override
  public void customize(JwtEncodingContext context) {

    if (context.getTokenType().getValue().equals("access_token")) {

      EcUserModel user = ecUserRepository.findByUsername(context.getPrincipal().getName())
          .orElseThrow(
              () -> new UsernameNotFoundException(AuthError.AUTH_ERROR_1.getDescription()));

      context.getClaims().claim("user_id", user.getUserId());
      context.getClaims().claim("role", user.getUserRole().getRoleDescription());
      context.getClaims().claim("email", user.getUsername());
    }
  }

}
