package bg.softuni.zooarchitect.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/", "/users/register", "/users/login", "/webfonts/**", "/sass/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(
                        formLogin -> {
                            formLogin.loginPage("/users/login");
                            formLogin.usernameParameter("username");
                            formLogin.passwordParameter("password");
                            formLogin.defaultSuccessUrl("/", true);
                            formLogin.failureUrl("/users/login?error");
                        }
                )
                .logout(
                        (logout) -> {
                            logout.logoutUrl("/users/logout");
                            logout.logoutSuccessUrl("/");
                            logout.invalidateHttpSession(true);
                        }
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
