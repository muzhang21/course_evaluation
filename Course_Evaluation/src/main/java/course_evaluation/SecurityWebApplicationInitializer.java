package course_evaluation;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

import course_evaluation.security.SecurityConfig;





public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

	public SecurityWebApplicationInitializer() {
		super(SecurityConfig.class);
	}
}
