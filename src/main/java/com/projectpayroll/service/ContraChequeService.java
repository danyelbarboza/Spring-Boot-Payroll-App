package com.projectpayroll.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projectpayroll.payroll.dao.FuncionarioBeneficioRepository;
import com.projectpayroll.payroll.dao.FuncionarioRepository;
import com.projectpayroll.payroll.entity.Beneficios;
import com.projectpayroll.payroll.entity.FuncionarioBeneficio;
import com.projectpayroll.payroll.entity.Funcionarios;
import com.projectpayroll.payroll.entity.ContraCheque;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.persistence.TypedQuery;
import java.util.Date;
import java.text.SimpleDateFormat;

@Service
public class ContraChequeService {

    private FuncionarioRepository funcionarioRepository;
    private FuncionarioBeneficioRepository funcionarioBeneficioRepository;
    private CalculadoraSalario calculadoraSalario;
    private EntityManager entityManager;

    @Autowired
    public ContraChequeService(FuncionarioRepository funcionarioRepository, FuncionarioBeneficioRepository funcionarioBeneficioRepository, CalculadoraSalario calculadoraSalario, EntityManager entityManager) {
        this.funcionarioRepository = funcionarioRepository;
        this.funcionarioBeneficioRepository = funcionarioBeneficioRepository;
        this.calculadoraSalario = calculadoraSalario;
        this.entityManager = entityManager;
    }

    public void calcularSalarioLiquido(Integer funcionarioId) {
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

        salvarContraCheque(funcionarioId, salarioBruto, salarioLiquido, inss, irrf);
    }

    public void salvarContraCheque(Integer funcionarioId, double salarioBruto, double salarioLiquido, double inss, double irrf) {
        ContraCheque contraCheque = new ContraCheque();
        contraCheque.setFuncionarioId(funcionarioId);
        contraCheque.setSalarioBruto(salarioBruto);
        contraCheque.setSalarioLiquido(salarioLiquido);
        contraCheque.setInss(inss);
        contraCheque.setIrrf(irrf);
        contraCheque.setValeTransporte(calculadoraSalario.getValeTransporteDeFuncionario(funcionarioId));
        contraCheque.setValeRefeicao(calculadoraSalario.getValeRefeicaoDeFuncionario(funcionarioId));
        contraCheque.setPlanoDeSaude(calculadoraSalario.getPlanoDeSaudeDeFuncionario(funcionarioId));
        contraCheque.setAuxilioCreche(calculadoraSalario.getAuxilioCrecheDeFuncionario(funcionarioId));
        contraCheque.setDataReferencia(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        save(contraCheque);
    }




    // inject entity manager using constructor injection
    @Autowired
    public ContraChequeService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // implement save method
    @Transactional
    public void save(ContraCheque item) {
        entityManager.persist(item);
    }

    public ContraCheque findById(Integer id) {
        return entityManager.find(ContraCheque.class, id);
    }

    public List<ContraCheque> findAll() {
        // create a query. Explanation: TypedQuery is a type-safe query. It is a query that returns a specific type of object.
        TypedQuery<ContraCheque> query = entityManager.createQuery("from ContraCheque Order By id_funcionario", ContraCheque.class);
        return query.getResultList();
    }

    public List<ContraCheque> findByIdFuncionario(Integer idFuncionario) {
        TypedQuery<ContraCheque> query = entityManager.createQuery("from ContraCheque where id_funcionario = :theData", ContraCheque.class);
        query.setParameter("theData", idFuncionario);
        return query.getResultList();
    }

    @Transactional
    public void update(ContraCheque item) {
        entityManager.merge(item);
    }

    @Transactional
    public void delete(Integer id) {
        ContraCheque item = findById(id);
        entityManager.remove(item);
    }

    @Transactional
    public int deleteAll() {
        int rowsDeleted = entityManager.createQuery("Delete from ContraCheque").executeUpdate();
        return rowsDeleted;
    }

}