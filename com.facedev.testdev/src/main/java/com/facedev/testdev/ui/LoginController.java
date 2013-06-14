package com.facedev.testdev.ui;

import com.facedev.testdev.ioc.annotation.Controller;
import com.facedev.testdev.ioc.annotation.WebMethod;
import com.facedev.testdev.security.UserBean;

@Controller
public class LoginController {

	@WebMethod("service/login")
	public UserBean doLogin() {
		return new UserBean("Alex", "Bereznevatiy");
	}
	
	@WebMethod("service/logout")
	public boolean doLogout() {
		return true;
	}
}
