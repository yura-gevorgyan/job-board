package am.itspace.jobboard.config;

import am.itspace.jobboard.entity.enums.Role;
import am.itspace.jobboard.security.CustomerOAth2UserService;
import am.itspace.jobboard.security.OAuth2AuthenticationSuccessHandler;
import am.itspace.jobboard.security.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailService userDetailService;
    private final CustomerOAth2UserService customerOAth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests

                        .requestMatchers("/login/**", "/register/**", "/forgot/**").permitAll()

                        .requestMatchers("/css/**").permitAll()
                        .requestMatchers("/js/**").permitAll()
                        .requestMatchers("/img/**").permitAll()
                        .requestMatchers("/fonts/**").permitAll()
                        .requestMatchers("/quform/**").permitAll()
                        .requestMatchers("/search/**").permitAll()
                        .requestMatchers("/v19/**").permitAll()
                        .requestMatchers("/css2/**").permitAll()
                        .requestMatchers("/download/picture/**").permitAll()
                        .requestMatchers("/getImage/**").permitAll()
                        .requestMatchers("/user").permitAll()
                        .requestMatchers("/user/emails").permitAll()

                        .requestMatchers("/").permitAll()
                        .requestMatchers("/jobs/**").permitAll()
                        .requestMatchers("/resumes/**").permitAll()
                        .requestMatchers("/companies/**").permitAll()

                        .requestMatchers("/jobs/favorites/**").hasAnyAuthority(Role.JOB_SEEKER.name(), Role.EMPLOYEE.name(), Role.COMPANY_OWNER.name())
                        .requestMatchers("/resumes/favorites/**").hasAnyAuthority(Role.JOB_SEEKER.name(), Role.EMPLOYEE.name(), Role.COMPANY_OWNER.name())
                        .requestMatchers("/companies/favorites/**").hasAnyAuthority(Role.JOB_SEEKER.name(), Role.EMPLOYEE.name(), Role.COMPANY_OWNER.name())

                        .requestMatchers("/jobs/apply/**").hasAnyAuthority(Role.JOB_SEEKER.name())
                        .requestMatchers("/resumes/apply/**").hasAnyAuthority(Role.EMPLOYEE.name(), Role.COMPANY_OWNER.name())

                        .requestMatchers("/resumes/create").hasAnyAuthority(Role.JOB_SEEKER.name())
                        .requestMatchers("/resumes/update").hasAnyAuthority(Role.JOB_SEEKER.name())

                        .requestMatchers("/profile/**").hasAnyAuthority(Role.JOB_SEEKER.name(), Role.EMPLOYEE.name(), Role.COMPANY_OWNER.name())
                        .requestMatchers("/profile/resume/**").hasAnyAuthority(Role.JOB_SEEKER.name())
                        .requestMatchers("/profile/company/**").hasAnyAuthority(Role.COMPANY_OWNER.name())
                        .requestMatchers("/profile/jobs-manage/**").hasAnyAuthority(Role.COMPANY_OWNER.name(), Role.EMPLOYEE.name())
                        .requestMatchers("/profile/jobs-create/**").hasAnyAuthority(Role.COMPANY_OWNER.name(), Role.EMPLOYEE.name())

                        .requestMatchers("/jobs/search-my-jobs").hasAnyAuthority(Role.EMPLOYEE.name(), Role.COMPANY_OWNER.name())
                        .requestMatchers("/jobs/create").hasAnyAuthority(Role.EMPLOYEE.name(), Role.COMPANY_OWNER.name())
                        .requestMatchers("/jobs/update").hasAnyAuthority(Role.EMPLOYEE.name(), Role.COMPANY_OWNER.name())
                        .requestMatchers("/jobs/create/company").hasAnyAuthority(Role.COMPANY_OWNER.name())
                        .requestMatchers("/jobs/update/company").hasAnyAuthority(Role.COMPANY_OWNER.name())
                        .requestMatchers("/jobs/delete/**").hasAnyAuthority(Role.EMPLOYEE.name(), Role.COMPANY_OWNER.name())
                        .requestMatchers("/jobs/return/**").hasAnyAuthority(Role.EMPLOYEE.name(), Role.COMPANY_OWNER.name())
                        .requestMatchers("/job/applies/**").hasAnyAuthority(Role.JOB_SEEKER.name())

                        .requestMatchers("/companies/create").hasAnyAuthority(Role.COMPANY_OWNER.name())
                        .requestMatchers("/companies/update").hasAnyAuthority(Role.COMPANY_OWNER.name())

                        .requestMatchers("/applicant/**").hasAnyAuthority(Role.JOB_SEEKER.name(), Role.EMPLOYEE.name(), Role.COMPANY_OWNER.name())

                        .requestMatchers("/profile/message/**").hasAnyAuthority(Role.JOB_SEEKER.name(), Role.EMPLOYEE.name(), Role.COMPANY_OWNER.name())
                        .requestMatchers("/messages/**").hasAnyAuthority(Role.JOB_SEEKER.name(), Role.EMPLOYEE.name(), Role.COMPANY_OWNER.name())
                        .requestMatchers("/delete-conversation/**").hasAnyAuthority(Role.JOB_SEEKER.name(), Role.EMPLOYEE.name(), Role.COMPANY_OWNER.name())

                        .requestMatchers("/user-jobs").hasAnyAuthority(Role.EMPLOYEE.name(), Role.COMPANY_OWNER.name())

                        .requestMatchers("/admin/**").hasAnyAuthority(Role.ADMIN.name())

                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler((request, response, accessDeniedException) -> request.getRequestDispatcher("/404-page").forward(request, response))
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/login")
                        .userInfoEndpoint(userInfoEndpoint ->
                                userInfoEndpoint.userService(customerOAth2UserService)
                        )
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .failureUrl("/login?error=true")
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .defaultSuccessUrl("/redirect", true)
                        .successHandler((request, response, authentication) -> {
                            response.sendRedirect("/");
                        })
                        .failureUrl("/login?error=true")
                )
                .rememberMe(rememberMe -> rememberMe
                        .tokenValiditySeconds(604800)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                );
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }
}
