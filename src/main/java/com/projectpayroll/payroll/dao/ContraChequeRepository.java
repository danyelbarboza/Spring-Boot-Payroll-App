package com.projectpayroll.payroll.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.projectpayroll.payroll.entity.Contracheque;

@RepositoryRestResource(path = "contracheques")
public interface ContrachequeRepository extends JpaRepository<Contracheque, Integer> {

}
