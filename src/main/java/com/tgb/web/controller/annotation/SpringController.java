package com.tgb.web.controller.annotation;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.support.RequestContextUtils;

@Controller
public class SpringController {
	
	@RequestMapping("/spring/get")
	public String get(HttpServletRequest request){
		//使用从上下文获取bean的方法，有时注解不能使用就可以用这个方法
		//spring上下文
		WebApplicationContext ac1=WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		//springMVC上下文
		WebApplicationContext ac2=RequestContextUtils.getWebApplicationContext(request);
		//获取spring的bean
		//ISpring springManager=(ISpring)ac1.getBean("springManager");
		ISpring springManager=(ISpring)ac2.getBean("springManager");//可以证明父子容器的关系，springmvc是spring的子容器，子容器拿到父容器的东西是正确的。
		System.out.println(springManager.get());
		return "/success";
	}
	@RequestMapping("/spring/login")
    public String login(HttpServletRequest request, String username, String password, Model model){
        //使用从上下文获取bean的方法，有时注解不能使用就可以用这个方法
        //spring上下文
        WebApplicationContext ac1=
            WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
        //springMVC上下文
        WebApplicationContext ac2=RequestContextUtils.getWebApplicationContext(request);
        //获取spring的bean
        //ISpring springManager=(ISpring)ac1.getBean("springManager");
        ISpring springManager=(ISpring)ac2.getBean("springManager");//可以证明父子容器的关系，springmvc是spring的子容器，子容器拿到父容器的东西是正确的。
        String result = "";
        try{
          result = springManager.login(username);
        } catch(Throwable e) {
          result = "登录失败，服务调用失败";
        }
        model.addAttribute("result", result);
        return "/success";
    }
}
