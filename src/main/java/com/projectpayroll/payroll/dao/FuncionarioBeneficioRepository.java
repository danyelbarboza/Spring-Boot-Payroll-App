package com.projectpayroll.payroll.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.projectpayroll.payroll.entity.FuncionarioBeneficio;

@RepositoryRestResource(path = "funcionario_beneficio")
public interface FuncionarioBeneficioRepository extends JpaRepository<FuncionarioBeneficio, Integer> {
    
    
    List<FuncionarioBeneficio> findByFuncionario_Id(Integer funcionarioId);
    

}
