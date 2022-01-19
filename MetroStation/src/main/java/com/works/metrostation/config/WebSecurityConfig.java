package com.works.metrostation.config;

import com.works.metrostation.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.*;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public UserDetailsService userDetailsService() { return new UserDetailsServiceImpl(); }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) {
        authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity.authorizeRequests()

                .antMatchers(AUTH_WHITELIST).permitAll()

                /*
                 * UserController authorization configuration
                 */
                .antMatchers(HttpMethod.POST, "/metro/users").permitAll()
                .antMatchers(HttpMethod.DELETE, "/metro/users/**").hasAuthority("ADMIN")
                .antMatchers("/metro/users/**").hasAnyAuthority("ADMIN","PASSANGER")

                /*
                 * MetroController authorization & authority configuration
                 */
                .antMatchers(HttpMethod.POST, "/metro/metros").hasAuthority("ADMIN")
                .antMatchers("/metro/metros/**").hasAuthority("ADMIN")

                /*
                 * Card authorization & authority configuration
                 */
                .antMatchers(HttpMethod.POST, "/metro/cards").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/metro/cards").hasAnyAuthority("ADMIN","PASSANGER")
                .antMatchers(HttpMethod.PUT, "/metro/cards").permitAll()
                .antMatchers(HttpMethod.DELETE, "/metro/cards/**").hasAuthority("ADMIN")

                /*
                 * VendingMachineController authorization & authority configuration
                 */
                .antMatchers(HttpMethod.POST, "/metro/boarding").hasAuthority("PASSANGER")

                /*
                 * VoyageController authorization & authority configuration
                 */
                .antMatchers(HttpMethod.POST, "/metro/voyages").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/metro/voyages/**").hasAnyAuthority("ADMIN","PASSANGER")

                /*
                 * Common security configurations
                 * Since it is an example application, basic auth is used for simplicity. (JWT or OAuth2 not implemented)
                 */
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .realmName("Metro Station API")
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .headers().frameOptions().sameOrigin();
    }

    private static final String[] AUTH_WHITELIST = {
            // -- Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**"
            // other public endpoints of your API may be appended to this array
    };

}