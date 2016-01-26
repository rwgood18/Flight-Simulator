package com.cooksys.frontend.beans.validation;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cooksys.frontend.beans.dao.UserDao;

/**
 * Username validation.
 * 
 * @author Russell Good
 */
@Component
@Scope("request")
public class UsernameValidator implements Validator {
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserDao userDao;

	/**
	 * Check to see if username already exists in database. If it does,
	 * don't let the user register that username. Notify the of the problem.
	 */
	@Override
	public void validate(FacesContext ctx, UIComponent component, Object value)
			throws ValidatorException {
		if (userDao.getUserByUsername((String) value) != null) {
			
			log.debug("Username already exists");
			throw new ValidatorException(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Error",
					"Username already exists"));
		}
	}
}