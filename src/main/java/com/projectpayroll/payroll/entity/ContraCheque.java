package com.projectpayroll.payroll.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "contracheque")
public class ContraCheque {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "id_funcionario")
    private Integer funcionarioId;
    
    @Column(name = "salario_bruto")
    private Double salarioBruto;
    
    @Column(name = "inss")
    private Double inss;
    
    @Column(name = "irrf")
    private Double irrf;
    
    @Column(name = "vale_transporte")
    private Double valeTransporte;

    @Column(name = "vale_refeição")
    private Double valeRefeicao;
    
    @Column(name = "plano_de_saude")
    private Double planoDeSaude;
    
    @Column(name = "auxilio_creche")
    private Double auxilioCreche;
    
    @Column(name = "salario_liquido")
    private Double salarioLiquido;

    @Column(name = "data_referencia")
    private String dataReferencia;


    //constructors
    public ContraCheque() {
    }

    public ContraCheque(Integer id, Integer funcionarioId, Double salarioBruto, Double inss, Double irrf, Double valeTransporte, Double valeRefeicao, Double planoDeSaude, Double auxilioCreche, Double salarioLiquido, String dataReferencia) {
        this.id = id;
        this.funcionarioId = funcionarioId;
        this.salarioBruto = salarioBruto;
        this.inss = inss;
        this.irrf = irrf;
        this.valeTransporte = valeTransporte;
        this.valeRefeicao = valeRefeicao;
        this.planoDeSaude = planoDeSaude;
        this.auxilioCreche = auxilioCreche;
        this.salarioLiquido = salarioLiquido;
        this.dataReferencia = dataReferencia;
    }

   

    

    //toString
    @Override
    public String toString() {
        return "employee [id=" + id + ", funcionarioId=" + funcionarioId + ", salarioBruto=" + salarioBruto + ", inss=" + inss + ", irrf=" + irrf + ", valeTransporte=" + valeTransporte + ", valeRefeicao=" + valeRefeicao + ", planoDeSaude=" + planoDeSaude + ", auxilioCreche=" + auxilioCreche + ", salarioLiquido=" + salarioLiquido + ", dataReferencia=" + dataReferencia + "]";
    }


     //getters and setters


    public void setId(Integer id) {
        this.id = id;
    }

    public void setFuncionarioId(Integer funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public void setSalarioBruto(Double salarioBruto) {
        this.salarioBruto = salarioBruto;
    }

    public void setInss(Double inss) {
        this.inss = inss;
    }

    public void setIrrf(Double irrf) {
        this.irrf = irrf;
    }

    public void setValeTransporte(Double valeTransporte) {
        this.valeTransporte = valeTransporte;
    }

    public void setValeRefeicao(Double valeRefeicao) {
        this.valeRefeicao = valeRefeicao;
    }

    public void setPlanoDeSaude(Double planoDeSaude) {
        this.planoDeSaude = planoDeSaude;
    }

    public void setAuxilioCreche(Double auxilioCreche) {
        this.auxilioCreche = auxilioCreche;
    }

    public void setSalarioLiquido(Double salarioLiquido) {
        this.salarioLiquido = salarioLiquido;
    }

    public void setDataReferencia(String dataReferencia) {
        this.dataReferencia = dataReferencia;
    }

    public Integer getId() {
        return id;
    }

    public Integer getFuncionarioId() {
        return funcionarioId;
    }

    public Double getSalarioBruto() {
        return salarioBruto;
    }

    public Double getInss() {
        return inss;
    }

    public Double getIrrf() {
        return irrf;
    }

    public Double getValeTransporte() {
        return valeTransporte;
    }

    public Double getValeRefeicao() {
        return valeRefeicao;
    }

    public Double getPlanoDeSaude() {
        return planoDeSaude;
    }

    public Double getAuxilioCreche() {
        return auxilioCreche;
    }

    public Double getSalarioLiquido() {
        return salarioLiquido;
    }

    public String getDataReferencia() {
        return dataReferencia;
    }
    
}