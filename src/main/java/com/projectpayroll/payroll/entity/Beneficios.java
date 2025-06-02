package com.projectpayroll.payroll.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "beneficios")
public class Beneficios {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "nome")
    private String name;
    
    @Column(name = "valor_padrao")
    private Double defaultValue;

    @Column(name = "eh_desconto")
    private Boolean isDiscount;
}
