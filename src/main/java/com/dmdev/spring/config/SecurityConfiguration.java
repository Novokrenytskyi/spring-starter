package com.dmdev.spring.config;

import com.dmdev.spring.database.entity.Role;
import com.dmdev.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Set;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                .csrf().disable()
                .authorizeHttpRequests(urlConfig -> urlConfig
                        .antMatchers("/login", "/users/registration", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                        .antMatchers("/users/{\\d+}/delete").hasAuthority(Role.ADMIN.getAuthority())
                        .antMatchers("/admin/**").hasAuthority(Role.ADMIN.getAuthority())
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .deleteCookies("JSESSIONID"))
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/users"))
                .oauth2Login(config -> config
                        .loginPage("/login")
                        .defaultSuccessUrl("/users")
                        .userInfoEndpoint(userInfo -> userInfo.oidcUserService(oidcUserService()))
                );
    }

    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        return userRequest -> {
            String email = userRequest.getIdToken().getClaim("email");
            UserDetails userDetails = userService.loadUserByUsername(email);
            //TODO 16.10.2023 create userService.create
//          new OidcUserService().loadUser()
            DefaultOidcUser oidcUser = new DefaultOidcUser(userDetails.getAuthorities(), userRequest.getIdToken());

            Set<Method> userDetailsMethods = Set.of(UserDetails.class.getMethods());

            return (OidcUser)Proxy.newProxyInstance(SecurityConfiguration.class.getClassLoader(),
                    new Class[]{UserDetails.class, OidcUser.class},
                    (proxy, method, args) -> userDetailsMethods.contains(method)
                            ? method.invoke(userDetails, args)
                            : method.invoke(oidcUser, args));
        };
    }
}
