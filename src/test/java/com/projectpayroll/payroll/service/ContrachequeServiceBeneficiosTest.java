package com.projectpayroll.payroll.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.projectpayroll.payroll.dao.FuncionarioBeneficioRepository;
import com.projectpayroll.payroll.dao.FuncionarioRepository;
import com.projectpayroll.payroll.entity.Beneficios;
import com.projectpayroll.payroll.entity.FuncionarioBeneficio;
import com.projectpayroll.payroll.entity.Funcionarios;

import jakarta.persistence.EntityManager;

@ExtendWith(MockitoExtension.class)
class ContrachequeServiceBeneficiosTest {

    @Mock
    private FuncionarioRepository funcionarioRepository;
    @Mock
    private FuncionarioBeneficioRepository funcionarioBeneficioRepository;
    @Mock
    private CalculadoraSalario calculadoraSalario;
    @Mock
    private GeradorPDFService geradorPDFService;
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    private ContrachequeService contrachequeService;

    private Funcionarios funcionario;

    @BeforeEach
    void setUp() {
        funcionario = new Funcionarios(1, "Funcionario Com Beneficios", "111.222.333-44", "Analista", 5000.00);
        when(calculadoraSalario.calcularINSS(5000.00)).thenReturn(500.0);
        when(calculadoraSalario.calcularIRRF(5000.00, 500.0)).thenReturn(400.0);
        when(funcionarioRepository.findById(1)).thenReturn(Optional.of(funcionario));
    }

    private void assertValorFormatado(String expected, Object actual) {
        String actualAsString = ((String) actual).replace(',', '.');
        assertEquals(expected, actualAsString);
    }

    @Test
    @DisplayName("Deve calcular salario liquido corretamente com beneficios de desconto")
    void deveCalcularSalarioComBeneficioDeDesconto() throws IOException {
        Beneficios planoSaude = new Beneficios(1, "Plano de Saúde", 200.00, true);
        Beneficios valeTransporte = new Beneficios(2, "Vale-Transporte", 100.00, true);
        FuncionarioBeneficio fb1 = new FuncionarioBeneficio(1, funcionario, planoSaude);
        FuncionarioBeneficio fb2 = new FuncionarioBeneficio(2, funcionario, valeTransporte);
        when(funcionarioBeneficioRepository.findByFuncionario_Id(1)).thenReturn(Arrays.asList(fb1, fb2));

        contrachequeService.calcularSalarioLiquido(1);

        ArgumentCaptor<Map<String, Object>> dadosCaptor = ArgumentCaptor.forClass(Map.class);
        verify(geradorPDFService).gerarContrachequePdf(dadosCaptor.capture(), anyString());
        Map<String, Object> dadosParaPdf = dadosCaptor.getValue();
        
        assertValorFormatado("3800.00", dadosParaPdf.get("valorLiquido"));
        assertValorFormatado("1200.00", dadosParaPdf.get("descontosDebitados"));
    }

    @Test
    @DisplayName("Deve calcular salario liquido corretamente com beneficios de credito")
    void deveCalcularSalarioComBeneficioDeCredito() throws IOException {
        Beneficios auxilioCreche = new Beneficios(1, "Auxílio Creche", 300.00, false);
        Beneficios valeRefeicao = new Beneficios(2, "Vale-Refeição", 400.00, false);
        FuncionarioBeneficio fb1 = new FuncionarioBeneficio(1, funcionario, auxilioCreche);
        FuncionarioBeneficio fb2 = new FuncionarioBeneficio(2, funcionario, valeRefeicao);
        when(funcionarioBeneficioRepository.findByFuncionario_Id(1)).thenReturn(Arrays.asList(fb1, fb2));

        contrachequeService.calcularSalarioLiquido(1);

        ArgumentCaptor<Map<String, Object>> dadosCaptor = ArgumentCaptor.forClass(Map.class);
        verify(geradorPDFService).gerarContrachequePdf(dadosCaptor.capture(), anyString());
        Map<String, Object> dadosParaPdf = dadosCaptor.getValue();

        assertValorFormatado("4800.00", dadosParaPdf.get("valorLiquido"));
        assertValorFormatado("700.00", dadosParaPdf.get("beneficiosCreditados"));
    }

    @Test
    @DisplayName("Deve calcular salario liquido corretamente com multiplos beneficios")
    void deveCalcularSalarioComMultiplosBeneficios() throws IOException {
        Beneficios planoSaude = new Beneficios(1, "Plano de Saúde", 150.00, true);
        Beneficios auxilioCreche = new Beneficios(2, "Auxílio Creche", 250.00, false);
        FuncionarioBeneficio fb1 = new FuncionarioBeneficio(1, funcionario, planoSaude);
        FuncionarioBeneficio fb2 = new FuncionarioBeneficio(2, funcionario, auxilioCreche);
        when(funcionarioBeneficioRepository.findByFuncionario_Id(1)).thenReturn(Arrays.asList(fb1, fb2));

        contrachequeService.calcularSalarioLiquido(1);

        ArgumentCaptor<Map<String, Object>> dadosCaptor = ArgumentCaptor.forClass(Map.class);
        verify(geradorPDFService).gerarContrachequePdf(dadosCaptor.capture(), anyString());
        Map<String, Object> dadosParaPdf = dadosCaptor.getValue();

        assertValorFormatado("1050.00", dadosParaPdf.get("descontosDebitados"));
        assertValorFormatado("250.00", dadosParaPdf.get("beneficiosCreditados"));
        assertValorFormatado("4200.00", dadosParaPdf.get("valorLiquido"));
    }
}