package com.lzrc.SistemaBancario.controllers;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.tomcat.util.security.MD5Encoder;
import org.aspectj.weaver.Utils;
import org.hibernate.type.descriptor.java.UUIDJavaType.ToBytesTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lzrc.SistemaBancario.db.PessoaTeste;
import com.lzrc.SistemaBancario.db.RepositoryPessoa;
import com.lzrc.SistemaBancario.request.cadastro.RequestCadastro;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class CadastrosController {
	
	@Autowired
	RepositoryPessoa r;
	
	@GetMapping("/cadastro")
	public ModelAndView cadastro(HttpSession httpSession, ModelAndView mv, HttpServletRequest http) {
		if(httpSession.isNew()==false && httpSession.getAttribute("pessoa")!=null) {
			httpSession.invalidate();
		}
		mv.setViewName("cadastro");
		mv.addObject("requestCadastro", new RequestCadastro());
		mv.addObject("login", "http://"+http.getServerName()+":"+http.getServerPort()+"/login");
		return mv;
	}
	
	@PostMapping("/cadastrar")
	public String cadastrar(RequestCadastro rc, RedirectAttributes redirect) {
	
		if(rc.getNome()!=null && rc.getNome().equals("") == false
		   && rc.getSenha()!=null && rc.getSenha().equals("")==false) {
			MessageDigest md5;
			String md5Password=null;
			try {
				md5 = MessageDigest.getInstance("MD5");
				md5.reset();
				md5.update(rc.getSenha().getBytes(), 0, rc.getSenha().getBytes().length);
				rc.setSenha(new BigInteger(1,md5.digest()).toString(16));
				
				System.out.println("senha codificada: "+rc.getSenha());
				
				} catch (NoSuchAlgorithmException e) {
				
				e.printStackTrace();
			}
			
			
			
			PessoaTeste pessoa=new PessoaTeste(rc.getNome(), rc.getSenha());
			pessoa.setSaldo(new BigDecimal(100));
			r.save(pessoa);
			redirect.addFlashAttribute("mensagem", "Cadastro realizado com sucesso!!");
			return "redirect:/cadastro";
		}
		redirect.addFlashAttribute("mensagem", "Falha ao cadastrar");
		return "redirect:/cadastro";
	}

}
