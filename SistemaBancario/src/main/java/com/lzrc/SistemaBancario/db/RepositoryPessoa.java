package com.lzrc.SistemaBancario.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryPessoa extends JpaRepository<PessoaTeste, Long>{
	
	PessoaTeste findByNome(String nome);
	
}
