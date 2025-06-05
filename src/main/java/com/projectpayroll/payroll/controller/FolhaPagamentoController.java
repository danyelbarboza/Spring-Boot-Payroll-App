
package com.projectpayroll.payroll.controller; 

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projectpayroll.payroll.entity.Contracheque;
import com.projectpayroll.payroll.service.ContrachequeService;

@RestController
@RequestMapping("/api/folha-pagamento")
public class FolhaPagamentoController {

    private final ContrachequeService contrachequeService;

    @Autowired
    public FolhaPagamentoController(ContrachequeService contrachequeService) {
        this.contrachequeService = contrachequeService;
    }

    // Endpoint para calcular a folha de pagamento de um funcionário
    @PostMapping("/calcular/{funcionarioId}")
    public ResponseEntity<String> calcularFolha(@PathVariable Integer funcionarioId) {
        try {
            contrachequeService.calcularSalarioLiquido(funcionarioId);
            return ResponseEntity.ok("Folha de pagamento calculada e salva para o funcionário ID: " + funcionarioId);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Calcular todos os funcionários
    @PostMapping("/calcular")
    public ResponseEntity<String> calcularFolha() {
        try {
            contrachequeService.calcularTodosSalariosLiquidos();
            return ResponseEntity.ok("Folha de pagamento calculada e salva para todos os funcionários");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/contracheques/{funcionarioId}")
    public ResponseEntity<List<Contracheque>> getContrachequesByFuncionarioId(@PathVariable Integer funcionarioId) {
        List<Contracheque> contracheques = contrachequeService.findByIdFuncionario(funcionarioId);
        return ResponseEntity.ok(contracheques);
    }

    @GetMapping("/contracheques")
    public ResponseEntity<List<Contracheque>> getAllContracheques() {
        List<Contracheque> contracheques = contrachequeService.findAll();
        return ResponseEntity.ok(contracheques);
    }

    @GetMapping("/contracheques/{id}")
    public ResponseEntity<Contracheque> getContrachequeById(@PathVariable Integer id) {
        Contracheque contracheque = contrachequeService.findById(id);
        return ResponseEntity.ok(contracheque);
    }

    @DeleteMapping("/contracheques/{id}")
    public ResponseEntity<String> deleteContracheque(@PathVariable Integer id) {
        try {
            contrachequeService.delete(id);
            return ResponseEntity.ok("Contracheque deletado com sucesso para o ID: " + id);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/contracheques")
    public ResponseEntity<String> deleteAllContracheques() {
        try {
            contrachequeService.deleteAll();
            return ResponseEntity.ok("Todos os contracheques foram deletados com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}