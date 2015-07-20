package springboot.boilerplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Grants a file-based and secure CORS configuration.
 * @author bruno
 *
 */
@Component
public class SpringBootBoilerplateCORSConfig implements Filter {

	@Autowired
	private Environment env;

	private final String anyDomainAllowed = "*";
	private List<String> listDomainsAllowed;
	private boolean isAnyDomainAllowed = false;

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) res;
		HttpServletRequest request = (HttpServletRequest) req;

		validateRequestOriginInDomainsAllowed(response, request);
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
		chain.doFilter(req, res);
	}

	private void validateRequestOriginInDomainsAllowed(
			final HttpServletResponse res, final HttpServletRequest req) {
		final String originRequest = req.getHeader("origin");

		if (listDomainsAllowed == null) {
			final String domainsAccessControlAllowOrigin = getDomainsAccessControlAllowOrigin();
			final String delimiter = ",";
			if (anyDomainAllowed.equals(domainsAccessControlAllowOrigin)) {
				isAnyDomainAllowed = true;
			}
			final String[] domainsAllowed = StringUtils.tokenizeToStringArray(
					domainsAccessControlAllowOrigin, delimiter);
			listDomainsAllowed = new ArrayList<String>(
					Arrays.asList(domainsAllowed));
		}
		if (isAnyDomainAllowed) {
			res.addHeader("Access-Control-Allow-Origin", anyDomainAllowed);
		} else if (listDomainsAllowed.contains(originRequest)
				&& org.apache.commons.validator.routines.UrlValidator
						.getInstance().isValid(originRequest)) {
			res.addHeader("Access-Control-Allow-Origin", originRequest);
		}
	}

	private String getDomainsAccessControlAllowOrigin() {
		return env.getProperty("domains.access.control.allow.origin");
	}

	public void init(FilterConfig filterConfig) {
	}

	public void destroy() {
	}
}