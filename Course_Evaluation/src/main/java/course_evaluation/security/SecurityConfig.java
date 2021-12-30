package course_evaluation.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
	public void configure(HttpSecurity http) throws Exception{
		
		//Temporary!! in order to access h2 database which is not available by default 
		//remove this when done
		http.csrf().disable(); 
		http.headers().frameOptions().disable();
		
		//only role user can access /secure
		http.authorizeRequests().antMatchers("/**").hasRole("USER")
			 .antMatchers("/addcourse").hasRole("ADMIN")
		//the directories as following are accessible to all
			.antMatchers("/","/js/**","/css/**", "/images/**").permitAll()
			
			//Temporary
			.antMatchers("/h2-console/**").permitAll()
			//other directories are accessible by authenticated user
			.anyRequest().denyAll()
		.and()
			//build the login page and direct to /secure page
			.formLogin().loginPage("/login").defaultSuccessUrl("/", true).permitAll()
		.and()
			.logout()
				.invalidateHttpSession(true)
				.clearAuthentication(true)
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login?logout")
				.permitAll();
		
			
		//.and()
		//	.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
			
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Override 
	public void configure(AuthenticationManagerBuilder auth) throws Exception{
		String password = passwordEncoder().encode("4444");
		String password2 = passwordEncoder().encode("3333");
		auth.inMemoryAuthentication()
		.withUser("zhang106@foo.com").password(password).roles("USER")
		.and()
		.withUser("kyson@168.com").password(password2).roles("ADMIN");
		
		
	}
	
	
}
