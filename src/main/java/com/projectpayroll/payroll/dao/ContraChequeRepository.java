package com.projectpayroll.payroll.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.projectpayroll.payroll.entity.ContraCheque;

@RepositoryRestResource(path = "contracheques")
public interface ContraChequeRepository extends JpaRepository<ContraCheque, Integer> {

}
