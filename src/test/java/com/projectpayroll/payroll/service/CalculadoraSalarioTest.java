package com.projectpayroll.payroll.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CalculadoraSalarioTest {

    private CalculadoraSalario calculadoraSalario;

    @BeforeEach
    void setUp() {
        calculadoraSalario = new CalculadoraSalario(null, null); // Passando null por enquanto
    }

    @Test
    @DisplayName("Deve calcular o INSS corretamente para a primeira faixa")
    void deveCalcularInssFaixa1() {
        double salarioBruto = 1500.00;
        double inssEsperado = 112.50; // 1500 * 7.5%
        double inssCalculado = calculadoraSalario.calcularINSS(salarioBruto);
        assertEquals(inssEsperado, inssCalculado, "O cálculo do INSS para a primeira faixa está incorreto.");
    }

    @Test
    @DisplayName("Deve calcular o INSS corretamente com múltiplas faixas")
    void deveCalcularInssMultiplasFaixas() {
        double salarioBruto = 3000.00;
        double inssEsperado = 253.41;
        double inssCalculado = calculadoraSalario.calcularINSS(salarioBruto);
        assertEquals(inssEsperado, inssCalculado, 0.01, "O cálculo do INSS para múltiplas faixas está incorreto.");
    }

    @Test
    @DisplayName("Deve calcular o IRRF corretamente para salário isento")
    void deveCalcularIrrfIsento() {
        double salarioBruto = 2500.00;
        double inss = calculadoraSalario.calcularINSS(salarioBruto);
        double baseCalculo = salarioBruto - inss;
        double irrfEsperado = 0.0;
        double irrfCalculado = calculadoraSalario.calcularIRRF(salarioBruto, inss);
        assertEquals(irrfEsperado, irrfCalculado, "O cálculo do IRRF para isentos está incorreto.");
    }

    @Test
    @DisplayName("Deve calcular o IRRF corretamente para a segunda faixa")
    void deveCalcularIrrfFaixa2() {
        double salarioBruto = 3000.00;
        double inss = 253.41;
        double baseCalculo = salarioBruto - inss;
        double irrfEsperado = 36.55;
        double irrfCalculado = calculadoraSalario.calcularIRRF(salarioBruto, inss);
        assertEquals(irrfEsperado, irrfCalculado, 0.01, "O cálculo do IRRF para a segunda faixa está incorreto.");
    }


}