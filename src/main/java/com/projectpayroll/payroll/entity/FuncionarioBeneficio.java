package com.projectpayroll.payroll.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "funcionario_beneficio")
public class FuncionarioBeneficio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "id_funcionario")
    private Funcionarios funcionario;
    
    @ManyToOne
    @JoinColumn(name = "id_beneficio_tipo")
    private Beneficios beneficio;
    
    //constructors
    public FuncionarioBeneficio() {
    }
    
    public FuncionarioBeneficio(Integer id, Funcionarios funcionario, Beneficios beneficio) {
        this.id = id;
        this.funcionario = funcionario;
        this.beneficio = beneficio;
    }

    //getters and setters
    public Integer getId() {
        return id;
    }

    public Funcionarios getFuncionario() {
        return funcionario;
    }

    public Beneficios getBeneficio() {
        return beneficio;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public void setFuncionario(Funcionarios funcionario) {
        this.funcionario = funcionario;
    }

    public void setBeneficio(Beneficios beneficio) {
        this.beneficio = beneficio;
    }
    
    //toString
    @Override
    public String toString() {
        return "FuncionarioBeneficio [id=" + id + ", funcionario=" + funcionario + ", beneficio=" + beneficio + "]";
    }
}
