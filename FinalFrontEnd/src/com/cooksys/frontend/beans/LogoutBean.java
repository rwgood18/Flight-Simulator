package com.cooksys.frontend.beans;

import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Remove User specific information from beans when they logout
 * 
 * @author Russell Good
 *
 */
@Component
@Scope("request")
public class LogoutBean {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TripBean tripBean;
	@Autowired
	private AuthenticationBean auth;

	// log out user and invalidate session
	public String logout() {
		log.debug("Logging out...");

		tripBean.lougout();
		tripBean.setShowHomeRoute(false);
		tripBean.setShowRoutes(false);

		FacesContext.getCurrentInstance().getExternalContext()
				.invalidateSession();
		log.debug("Logout success");
		return "logged-out";
	}
}
