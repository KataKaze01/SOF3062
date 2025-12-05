package poly.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import poly.demo.service.UserInfoService;

@Controller
public class MyController {
	@RequestMapping("/")
	public String home(Model model) {	
		
		model.addAttribute("message", "@/ => home()");
		return "page";
	}
	
	@Autowired
	UserInfoService userInfoService;
	
	@RequestMapping("/poly/url0")
	public String method0(Model model) {	
		String username = userInfoService.getUsername();
		var roles = userInfoService.getRoles();
		System.out.println(username);
		System.out.println(roles.toString());
		
		model.addAttribute("message", "@/poly/url0 => method1()");
		return "page";
	}
	
	@RequestMapping("/poly/url1")
	public String method1(Model model) {
		model.addAttribute("message", "@/poly/url1 => method1()");
		return "page";
	}
	
	@RequestMapping("/poly/url2")
	public String method2(Model model) {
		model.addAttribute("message", "@/poly/url2 => method2()");
		return "page";
	}
	
	@RequestMapping("/poly/url3")
	public String method3(Model model) {
		model.addAttribute("message", "@/poly/url3 => method3()");
		return "page";
	}
	
	@RequestMapping("/poly/url4")
	public String method4(Model model) {
		model.addAttribute("message", "@/poly/url4 => method4()");
		return "page";
	}
}