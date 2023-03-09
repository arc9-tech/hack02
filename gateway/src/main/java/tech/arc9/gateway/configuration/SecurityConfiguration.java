package tech.arc9.gateway.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);

    @Autowired private GatewayConfig config;

    private static final String[] AUTH_LIST = {
            "/v2/api-docs",
            "/swagger-ui.html",
    };

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        String username = config.getSwaggerUsername();
        String password = config.getSwaggerPassword();

        auth.inMemoryAuthentication().withUser("user").password(passwordEncoder()
                        .encode("password")).roles("USER").and().withUser(username)
                .password(passwordEncoder().encode(password)).roles("USER", "ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests().antMatchers(AUTH_LIST)
                .authenticated().and().httpBasic();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}