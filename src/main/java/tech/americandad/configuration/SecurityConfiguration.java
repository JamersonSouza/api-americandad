package tech.americandad.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import tech.americandad.constants.SecurityConstant;
import tech.americandad.jwtFilter.JWTAuthorizationFilter;
import tech.americandad.jwtFilter.JwtAcessoDeniedHandler;
import tech.americandad.jwtFilter.JwtAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{


    private JWTAuthorizationFilter jwtAuthorizationFilter;
    private JwtAcessoDeniedHandler jwtAcessoDeniedHandler;
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private UserDetailsService userDetailsService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    
    

    @Autowired
    public SecurityConfiguration(
            JWTAuthorizationFilter jwtAuthorizationFilter,
            JwtAcessoDeniedHandler jwtAcessoDeniedHandler, 
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            @Qualifier("userDetailsService")UserDetailsService userDetailsService, 
            BCryptPasswordEncoder bCryptPasswordEncoder) 
        {
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
        this.jwtAcessoDeniedHandler = jwtAcessoDeniedHandler;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
       
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)  //m??todo que assegura que a sess??o de usu??rio n??o vai ser criada
            .and().authorizeRequests().antMatchers(SecurityConstant.URLS_PUBLICA).permitAll()
            .anyRequest().authenticated()
            .and()
            .exceptionHandling().accessDeniedHandler(jwtAcessoDeniedHandler)
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .and()
            .addFilterBefore(jwtAuthorizationFilter, 
            UsernamePasswordAuthenticationFilter.class);
    }
    

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

}
