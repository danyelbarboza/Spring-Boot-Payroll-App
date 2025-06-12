package com.projectpayroll.payroll.service;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.projectpayroll.payroll.dao.FuncionarioBeneficioRepository;
import com.projectpayroll.payroll.dao.FuncionarioRepository;
import com.projectpayroll.payroll.entity.Funcionarios;

import jakarta.persistence.EntityManager;

@ExtendWith(MockitoExtension.class)
class ContrachequeServiceTest {

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
        funcionario = new Funcionarios(1, "Danyel Teste", "123.456.789-00", "Desenvolvedor", 5000.00);
    }

    @Test
    void quandoCalcularSalarioLiquido_entaoDeveSalvarContracheque() throws Exception {
        when(funcionarioRepository.findById(1)).thenReturn(Optional.of(funcionario));

        when(funcionarioBeneficioRepository.findByFuncionario_Id(1)).thenReturn(Collections.emptyList());

        when(calculadoraSalario.calcularINSS(5000.00)).thenReturn(482.53);
        when(calculadoraSalario.calcularIRRF(5000.00, 482.53)).thenReturn(373.97);

        doNothing().when(geradorPDFService).gerarContrachequePdf(anyMap(), anyString());

        contrachequeService.calcularSalarioLiquido(1);
        verify(funcionarioRepository, times(1)).findById(1);
        verify(entityManager, times(1)).persist(any());
        verify(geradorPDFService, times(1)).gerarContrachequePdf(anyMap(), eq("Danyel Teste"));
    }
}