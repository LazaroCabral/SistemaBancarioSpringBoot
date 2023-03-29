package com.lzrc.SistemaBancario.request.transferencia;

import java.math.BigDecimal;

public class RequestTransferencias {
	private String conta;
	private Double valor;
	private Double valorCentavos;
	
	public String getConta() {
		return conta;
	}
	public void setConta(String conta) {
		this.conta = conta;
	}
	public Double getValor() {
		return valor;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}
	public Double getValorCentavos() {
		return valorCentavos;
	}
	public void setValorCentavos(Double valorCentavos) {
		this.valorCentavos = valorCentavos;
	}
	

	
	
	
	

	

}
