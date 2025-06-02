package com.projectpayroll.payroll.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "funcionarios")
public class Funcionarios {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "nome")
    private String name;
    
    @Column(name = "cpf")
    private String cpf;
    
    @Column(name = "cargo")
    private String position;
    
    
    @Column(name = "salario_bruto")
    private Double grossSalary;

    //constructors
    public Funcionarios() {
    }

    public Funcionarios(Integer id, String name, String cpf, String position, Double grossSalary) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.position = position;
        this.grossSalary = grossSalary;
    }

    //getters and setters

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCpf() {
        return cpf;
    }

    public String getPosition() {
        return position;
    }

    public Double getGrossSalary() {
        return grossSalary;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setGrossSalary(Double grossSalary) {
        this.grossSalary = grossSalary;
    }

    //toString
    @Override
    public String toString() {
        return "employee [id=" + id + ", name=" + name + ", cpf=" + cpf + ", position=" + position + ", grossSalary="
                + grossSalary + "]";
    }
    
}