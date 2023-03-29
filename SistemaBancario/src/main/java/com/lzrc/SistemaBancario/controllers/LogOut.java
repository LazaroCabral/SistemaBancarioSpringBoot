package com.lzrc.SistemaBancario.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

@Controller
public class LogOut {
	
	@GetMapping("/logout")
	public String logOut(HttpSession http, RedirectAttributes redirect) {
		if(http.isNew()==false || http.getAttribute("pessoa")!=null) {
			http.invalidate();
			redirect.addFlashAttribute("mensagem", "logout realizado com sucesso!");
			return "redirect:/login";
		}
		return "redirect:/login";
	}
	

}
