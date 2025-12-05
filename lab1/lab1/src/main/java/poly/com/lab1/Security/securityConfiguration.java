package poly.com.lab1.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class securityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder pe) {
        String password = pe.encode("123");
        UserDetails user1 = User.withUsername("user@gmail.com").password(password).roles("USER").build();
        UserDetails user2 = User.withUsername("admin@gmail.com").password(password).roles("ADMIN").build();
        UserDetails user3 = User.withUsername("both@gmail.com").password(password).roles("USER", "ADMIN").build();
        return new InMemoryUserDetailsManager(user1, user2, user3);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/poly/**").authenticated()
                        .anyRequest().permitAll()
                )

                // 🔐 FORM LOGIN TÙY BIẾN
                .formLogin(config -> config
                        .loginPage("/login/form")             // Trang form đăng nhập
                        .loginProcessingUrl("/login/check")         // URL xử lý POST (action form)
                        .defaultSuccessUrl("/login/success", true) // Khi đăng nhập thành công
                        .failureUrl("/login/failure")         // Khi đăng nhập thất bại
                        .permitAll()
                        .usernameParameter("username")
                        .passwordParameter("password")
                )

                // 🔐 LOGOUT TÙY BIẾN
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login/exit")
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .deleteCookies("remember-me")
                )

                // ✅ Ghi nhớ đăng nhập
                .rememberMe(config -> config
                        .tokenValiditySeconds(3 * 24 * 60 * 60)
                        .rememberMeCookieName("remember-me")
                        .rememberMeParameter("remember-me")
                );

        return http.build();
    }
}
