package com.lzrc.SistemaBancario.controllers;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lzrc.SistemaBancario.db.PessoaTeste;
import com.lzrc.SistemaBancario.db.RepositoryPessoa;
import com.lzrc.SistemaBancario.request.login.RequestLogin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	@Autowired
	RepositoryPessoa r;
	

	@GetMapping("/login")
	public ModelAndView login(HttpSession http, ModelAndView mv, RequestLogin rl, HttpServletRequest hsr ) {
		mv.setViewName("login");
		mv.addObject("requestlogin", rl);
		mv.addObject("cadastrar", "http://"+hsr.getServerName()+":"+hsr.getServerPort()+"/cadastro");
		return mv;
	}
	
	@PostMapping("/loginpost")
	public String loginPost(HttpSession http, RedirectAttributes redirect, 
			RequestLogin rl, HttpServletRequest hsr) {
		ModelAndView mv= new ModelAndView("login");
		System.out.println(rl.getNome());
		
		MessageDigest md5;
		String md5Pasword=null;
		try {
			md5=MessageDigest.getInstance("MD5");
			md5.reset();
			md5.update(rl.getSenha().getBytes(), 0, rl.getSenha().getBytes().length);
			rl.setSenha(new BigInteger(1, md5.digest()).toString(16));
		} catch (NoSuchAlgorithmException e) {
			
			e.printStackTrace();
		}
		if(rl.getNome()==null || rl.getNome().equals("")) {
			redirect.addFlashAttribute("mensagem", "o nome não pode ser nulo");
			return "redirect:login";
		}
		if(rl.getSenha()==null || rl.getSenha().equals("")) {
			redirect.addFlashAttribute("mensagem", "a senha não pode ser nula");
			return "redirect:login";
		}
		PessoaTeste pessoa=r.findByNome(rl.getNome());
		if(pessoa==null) {
			redirect.addFlashAttribute("mensagem", "Usuario não encontrado");
			return "redirect:login";
		}
		if(pessoa.getNome().equals(rl.getNome()) && pessoa.getSenha().equals(rl.getSenha())) {
			http.invalidate();
			http = hsr.getSession();
			http.setAttribute("pessoa", pessoa);
			redirect.addFlashAttribute("mensagem", "bem vindo!");
			return "redirect:/transferencia";
		}
		if(pessoa.getSenha().equals(rl.getSenha())==false) {
			redirect.addFlashAttribute("mensagem", "senha incorreta!");
			return "redirect:/login";

		}
		redirect.addFlashAttribute("mensagem", "usuario não encontrado!!");
		return "redirect:login";
	}
}
