package com.projectpayroll.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projectpayroll.payroll.dao.FuncionarioBeneficioRepository;
import com.projectpayroll.payroll.dao.FuncionarioRepository;
import com.projectpayroll.payroll.entity.Beneficios;
import com.projectpayroll.payroll.entity.FuncionarioBeneficio;
import com.projectpayroll.payroll.entity.Funcionarios;

@Service
public class ContraCheque {

    private final FuncionarioRepository funcionarioRepository;
    private final FuncionarioBeneficioRepository funcionarioBeneficioRepository;
    private final CalculadoraSalario calculadoraSalario;

    @Autowired
    public ContraCheque(FuncionarioRepository funcionarioRepository, FuncionarioBeneficioRepository funcionarioBeneficioRepository, CalculadoraSalario calculadoraSalario) {
        this.funcionarioRepository = funcionarioRepository;
        this.funcionarioBeneficioRepository = funcionarioBeneficioRepository;
        this.calculadoraSalario = calculadoraSalario;
    }

    public double calcularSalarioLiquido(Integer funcionarioId) {
        Funcionarios funcionario = funcionarioRepository.findById(funcionarioId).orElseThrow(() -> new RuntimeException("Funcionário não encontrado")); // Tratar exceção adequadamente

        double salarioBruto = funcionario.getGrossSalary();
        double totalDescontosBeneficios = 0.0;
        double totalAcrescimosBeneficios = 0.0;

        // Lógica para buscar FuncionarioBeneficio e somar/subtrair do salário
        // Exemplo:
        List<FuncionarioBeneficio> beneficiosFuncionario = funcionarioBeneficioRepository.findByFuncionario_Id(funcionarioId);
        for (FuncionarioBeneficio item : beneficiosFuncionario) {
            Beneficios beneficio = item.getBeneficio();
            if (beneficio.getIsDiscount()) {
                totalDescontosBeneficios += beneficio.getDefaultValue();
            } else {
                totalAcrescimosBeneficios += beneficio.getDefaultValue();
            }
        }

        double inss = calculadoraSalario.calcularINSS(salarioBruto);
        double irrf = calculadoraSalario.calcularIRRF(salarioBruto, inss);

        double salarioLiquido = salarioBruto - inss - irrf - totalDescontosBeneficios + totalAcrescimosBeneficios;

        return salarioLiquido;
    }

}