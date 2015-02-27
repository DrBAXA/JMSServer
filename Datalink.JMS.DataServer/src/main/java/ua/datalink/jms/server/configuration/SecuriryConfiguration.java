package ua.datalink.jms.server.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@EnableWebSecurity
public class SecuriryConfiguration extends WebSecurityConfigurerAdapter{


    @Autowired
    Environment env;

    @Autowired
    public void configGlobal(AuthenticationManagerBuilder auth) throws Exception {
        final String userName = env.getProperty("server.user");
        final String userPassword = env.getProperty("server.password");
        auth.inMemoryAuthentication()
                .withUser(userName)
                .password(userPassword)
                .roles("ADMIN");
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/*").hasRole("ADMIN")
                .antMatchers("/admin*").hasRole("ADMIN")
                .and()

                .httpBasic();
    }
}
