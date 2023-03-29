package com.lzrc.SistemaBancario.controllers;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lzrc.SistemaBancario.db.PessoaTeste;
import com.lzrc.SistemaBancario.db.RepositoryPessoa;
import com.lzrc.SistemaBancario.request.transferencia.RequestTransferencias;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class TransferenciasController {
	
	@Autowired
	RepositoryPessoa r;
	
	@GetMapping("/transferencia")
	public Object getpage(HttpSession http, RedirectAttributes redirect, RequestTransferencias rt, ModelAndView mv, HttpServletRequest hsr) {
		if(http.isNew() || http.getAttribute("pessoa")==null) {
			redirect.addFlashAttribute("mensagem", "Voce deve se logar primeiro");
			return "redirect:login";
		}
		mv.setViewName("transferencias");
		mv.addObject("logout", "http://"+hsr.getServerName()+":"+hsr.getServerPort()+"/logout");
		return mv;
	}
	
	@PostMapping("/transferir")
	public String transferir(HttpSession http, RedirectAttributes redirect, RequestTransferencias rt) {
		try {
			Double valor=(double) (rt.getValorCentavos()/100)+rt.getValor();
			if(rt.getValorCentavos()>=100 || rt.getValorCentavos()<0 || rt.getValor()<0) {
				redirect.addFlashAttribute("mensagem", "valor invalido");
				return "redirect:/transferencia";
			}
			if(r.findByNome(rt.getConta())!=null) {
				PessoaTeste contaSelecionada=r.findByNome(rt.getConta());
				PessoaTeste conta=(PessoaTeste)http.getAttribute("pessoa");
				conta=r.findByNome(conta.getNome());
				
				if(rt.getValor().doubleValue()<=conta.getSaldo().doubleValue()) {
					conta.setSaldo(new BigDecimal(conta.getSaldo().doubleValue()-valor));
					contaSelecionada.setSaldo(new BigDecimal(contaSelecionada.getSaldo().doubleValue()+valor));
					r.save(conta);
					r.save(contaSelecionada);
					redirect.addFlashAttribute("mensagem", "Transferencia realizada com sucesso");
					return "redirect:/transferencia";
				}
				else {
					redirect.addFlashAttribute("mensagem", "Saldo insuficiente");
					return "redirect:/transferencia";
				}
			}
			
			redirect.addFlashAttribute("mensagem", "Conta não encontrada");
			return "redirect:transferencia";
		}catch(NullPointerException e) {
			redirect.addFlashAttribute("mensagem", "Os campos não podem ser nulos!");
			return "redirect:/transferencia";
		}
		
	}
}
