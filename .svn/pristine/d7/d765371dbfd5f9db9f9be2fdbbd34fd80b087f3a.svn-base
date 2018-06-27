package com.tgb.web.controller.annotation;

import com.alibaba.dubbo.demo.DemoService;
import com.csii.user.LoginService;

public class SpringManager implements ISpring{
	private DemoService demoService;
	private LoginService loginService;
	
	

	public LoginService getLoginService() {
    return loginService;
  }


  public void setLoginService(LoginService loginService) {
    this.loginService = loginService;
  }


  public DemoService getDemoService() {
		return demoService;
	}


	public void setDemoService(DemoService demoService) {
		this.demoService = demoService;
	}


	public String get() {
		//调用远程服务
		System.out.println(demoService.sayHello("world"));
		System.out.println("---i am SpringManager---");
		return "i am getMethod";
	}


  public String login(String username) {
    return loginService.login(username);
  }

}
