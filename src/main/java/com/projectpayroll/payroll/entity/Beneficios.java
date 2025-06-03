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

    //constructors
    public Beneficios() {
    }
    
    public Beneficios(Integer id, String name, Double defaultValue, Boolean isDiscount) {
        this.id = id;
        this.name = name;
        this.defaultValue = defaultValue;
        this.isDiscount = isDiscount;
    }

    //getters and setters

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getDefaultValue() {
        return defaultValue;
    }

    public Boolean getIsDiscount() {
        return isDiscount;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDefaultValue(Double defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setIsDiscount(Boolean isDiscount) {
        this.isDiscount = isDiscount;
    }

}
