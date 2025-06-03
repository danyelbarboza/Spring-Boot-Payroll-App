package com.projectpayroll.payroll.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.projectpayroll.payroll.entity.ContraCheque;

@RepositoryRestResource(path = "contracheque")
public interface ContraChequeRepository extends JpaRepository<ContraCheque, Integer> {
    
}
