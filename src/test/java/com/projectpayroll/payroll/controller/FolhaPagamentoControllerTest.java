package com.projectpayroll.payroll.controller;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.projectpayroll.payroll.service.ContrachequeService;

@WebMvcTest(FolhaPagamentoController.class)
class FolhaPagamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContrachequeService contrachequeService;

    @Test
    void quandoCalcularFolha_deveRetornarStatusOk() throws Exception {
        doNothing().when(contrachequeService).calcularSalarioLiquido(1);

        mockMvc.perform(post("/api/folha-pagamento/calcular/{funcionarioId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("Folha de pagamento calculada e salva para o funcionário ID: 1"));
    }

    @Test
    void quandoCalcularFolhaComFuncionarioInexistente_deveRetornarStatusBadRequest() throws Exception {
        doThrow(new RuntimeException("Funcionário não encontrado"))
            .when(contrachequeService).calcularSalarioLiquido(99);

        mockMvc.perform(post("/api/folha-pagamento/calcular/{funcionarioId}", 99))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Funcionário não encontrado"));
    }
}