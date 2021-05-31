package pl.pai.pai.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    protected void configure(HttpSecurity http) throws Exception {
    	http.csrf().disable()
	        .requestCache().requestCache(new CustomRequestCache()) 
	        .and().authorizeRequests() 
	        .antMatchers("/registration").permitAll()
	        .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()  
	        //.antMatchers("/registration").permitAll()
	        //.anyRequest().authenticated()  
	        .anyRequest().authenticated()
	        .and()
	        .formLogin()  
	        .loginPage("/login").permitAll()
	        .usernameParameter("username")
            .passwordParameter("password")
	        .loginProcessingUrl("/login_process")  
	        .failureUrl("/login?error=true")
	        .defaultSuccessUrl("/")
	        .and()
	        .logout()
	        .logoutUrl("/logout")
            .logoutSuccessUrl("/login");
	        


    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
            "/VAADIN/**",
            "/favicon.ico",
            "/robots.txt",
            "/manifest.webmanifest",
            "/sw.js",
            "/offline.html",
            "/icons/**",
            "/images/**",
            "/styles/**",
            "/h2-console/**",
            "/registration"
        	);
    }
    @Autowired
    DataSource dataSource;
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    	System.out.println("Robie");
        auth.jdbcAuthentication()
                .usersByUsernameQuery("SELECT email, password, 'true' as enabled FROM users WHERE email=?")
                .authoritiesByUsernameQuery(
                        "SELECT email, 'ROLE_USER' FROM users WHERE email=?")
                .dataSource(dataSource)
                .passwordEncoder(new BCryptPasswordEncoder());
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }
}
