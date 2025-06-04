
package com.projectpayroll.payroll.controller; 

import com.projectpayroll.payroll.service.ContraChequeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/folha-pagamento")
public class FolhaPagamentoController {

    private final ContraChequeService contraChequeService;

    @Autowired
    public FolhaPagamentoController(ContraChequeService contraChequeService) {
        this.contraChequeService = contraChequeService;
    }

    // Endpoint para calcular a folha de pagamento de um funcionário
    @PostMapping("/calcular/{funcionarioId}")
    public ResponseEntity<String> calcularFolha(@PathVariable Integer funcionarioId) {
        try {
            contraChequeService.calcularSalarioLiquido(funcionarioId);
            return ResponseEntity.ok("Folha de pagamento calculada e salva para o funcionário ID: " + funcionarioId);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}