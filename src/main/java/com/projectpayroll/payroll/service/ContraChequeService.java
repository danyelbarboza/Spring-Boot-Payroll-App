package com.projectpayroll.payroll.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projectpayroll.payroll.dao.FuncionarioBeneficioRepository;
import com.projectpayroll.payroll.dao.FuncionarioRepository;
import com.projectpayroll.payroll.entity.Beneficios;
import com.projectpayroll.payroll.entity.ContraCheque;
import com.projectpayroll.payroll.entity.FuncionarioBeneficio;
import com.projectpayroll.payroll.entity.Funcionarios;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

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

    @Transactional
    public void calcularSalarioLiquido(Integer funcionarioId) {
        Funcionarios funcionario = funcionarioRepository.findById(funcionarioId).orElseThrow(() -> new RuntimeException("Funcionário não encontrado")); // Tratar exceção adequadamente

        double salarioBruto = funcionario.getGrossSalary();
        double valeTransporte = 0.0;
        double valeRefeicao = 0.0;
        double planoDeSaude = 0.0;
        double auxilioCreche = 0.0;

        // Lógica para buscar FuncionarioBeneficio e somar/subtrair do salário
        List<FuncionarioBeneficio> beneficiosFuncionario = funcionarioBeneficioRepository.findByFuncionario_Id(funcionarioId);
        for (FuncionarioBeneficio item : beneficiosFuncionario) {
            Beneficios beneficio = item.getBeneficio();
                if (beneficio.getName().equalsIgnoreCase("Plano de Saúde")) {
                    planoDeSaude += beneficio.getDefaultValue();
                } else if (beneficio.getName().equalsIgnoreCase("Vale-Transporte")) {
                    valeTransporte += beneficio.getDefaultValue();
                } else if (beneficio.getName().equalsIgnoreCase("Vale-Refeição")) {
                    valeRefeicao += beneficio.getDefaultValue();
                } else if (beneficio.getName().equalsIgnoreCase("Auxílio Creche")) {
                    auxilioCreche += beneficio.getDefaultValue();
                }
            }

        double inss = calculadoraSalario.calcularINSS(salarioBruto);
        double irrf = calculadoraSalario.calcularIRRF(salarioBruto, inss);

        double salarioLiquido = salarioBruto - inss - irrf - planoDeSaude - valeTransporte + valeRefeicao + auxilioCreche;

        salvarContraCheque(funcionarioId, salarioBruto, salarioLiquido, inss, irrf, valeTransporte, valeRefeicao, planoDeSaude, auxilioCreche);
    }

    @Transactional
    public void salvarContraCheque(Integer funcionarioId, double salarioBruto, double salarioLiquido, double inss, double irrf, double valeTransporte, double valeRefeicao, double planoDeSaude, double auxilioCreche) {
        ContraCheque contraCheque = new ContraCheque();
        contraCheque.setFuncionarioId(funcionarioId);
        contraCheque.setSalarioBruto(salarioBruto);
        contraCheque.setSalarioLiquido(salarioLiquido);
        contraCheque.setInss(inss);
        contraCheque.setIrrf(irrf);
        contraCheque.setValeTransporte(valeTransporte);
        contraCheque.setValeRefeicao(valeRefeicao);
        contraCheque.setPlanoDeSaude(planoDeSaude);
        contraCheque.setAuxilioCreche(auxilioCreche);
        contraCheque.setDataReferencia(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        save(contraCheque);
    }


    @Transactional
    public void calcularTodosSalariosLiquidos() {
        List<Funcionarios> funcionarios = funcionarioRepository.findAll();
        for (Funcionarios funcionario : funcionarios) {
            calcularSalarioLiquido(funcionario.getId());
        }
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