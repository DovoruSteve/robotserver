package au.com.dovoru.robot.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	private String username = "kizzie";			// TODO: In a real system, this should be expanded
	private String password = "HappyHippo";
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
		.csrf().disable()		// TODO: Enable for Production application
		.authorizeHttpRequests(
				(authz) -> authz.requestMatchers("/authenticate")
				.permitAll().anyRequest().authenticated()

		).httpBasic(Customizer.withDefaults());
		return http.build();
	}

	@Bean
	public InMemoryUserDetailsManager userDetailsService() {
		UserDetails user = User.withUsername(username)
				.password(passwordEncoder().encode(password))
				.roles("USER_ROLE")
				.build();
		return new InMemoryUserDetailsManager(user);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(8);
	}
}