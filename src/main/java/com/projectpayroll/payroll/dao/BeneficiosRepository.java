package com.projectpayroll.payroll.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.projectpayroll.payroll.entity.Beneficios;

@RepositoryRestResource(path = "beneficios")
public interface BeneficiosRepository extends JpaRepository<Beneficios, Integer> {
    
}
