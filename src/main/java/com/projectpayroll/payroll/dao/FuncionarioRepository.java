package com.projectpayroll.payroll.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.projectpayroll.payroll.entity.Funcionarios;

@RepositoryRestResource(path = "funcionarios")
public interface FuncionarioRepository extends JpaRepository<Funcionarios, Integer> {
    
}
