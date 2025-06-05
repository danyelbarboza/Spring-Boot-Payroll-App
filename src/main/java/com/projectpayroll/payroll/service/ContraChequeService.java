package com.projectpayroll.payroll.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projectpayroll.payroll.dao.FuncionarioBeneficioRepository;
import com.projectpayroll.payroll.dao.FuncionarioRepository;
import com.projectpayroll.payroll.entity.Beneficios;
import com.projectpayroll.payroll.entity.Contracheque;
import com.projectpayroll.payroll.entity.FuncionarioBeneficio;
import com.projectpayroll.payroll.entity.Funcionarios;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Service
public class ContrachequeService {

    private FuncionarioRepository funcionarioRepository;
    private FuncionarioBeneficioRepository funcionarioBeneficioRepository;
    private CalculadoraSalario calculadoraSalario;
    private EntityManager entityManager;
    private GeradorPDFService geradorPDFService;
    
    @Autowired
    public ContrachequeService(FuncionarioRepository funcionarioRepository, FuncionarioBeneficioRepository funcionarioBeneficioRepository, CalculadoraSalario calculadoraSalario, EntityManager entityManager, GeradorPDFService geradorPDFService) {
        this.funcionarioRepository = funcionarioRepository;
        this.funcionarioBeneficioRepository = funcionarioBeneficioRepository;
        this.calculadoraSalario = calculadoraSalario;
        this.entityManager = entityManager;
        this.geradorPDFService = geradorPDFService;
    }

    @Transactional
    public void calcularSalarioLiquido(Integer funcionarioId) {
        Funcionarios funcionario = funcionarioRepository.findById(funcionarioId).orElseThrow(() -> new RuntimeException("Funcionário não encontrado")); // Tratar exceção adequadamente

        double salarioBruto = funcionario.getGrossSalary();
        double valeTransporte = 0.0;
        double valeRefeicao = 0.0;
        double planoDeSaude = 0.0;
        double auxilioCreche = 0.0;
        String dataReferencia = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

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

        double beneficiosCreditados = valeRefeicao + auxilioCreche;
        double descontosDebitados = inss + irrf + planoDeSaude + valeTransporte;

        String nome = funcionario.getName();
        String cpf = funcionario.getCpf();
        String cargo = funcionario.getPosition();
        double fgtsMes = salarioBruto * 0.08;

         // Criar um Map para passar os dados para o GeradorPDFService
        Map<String, Object> dadosParaPdf = new HashMap<>();
        dadosParaPdf.put("Empresa", "Nome da Empresa Exemplo LTDA."); 
        dadosParaPdf.put("CNPJ", "00.000.000/0001-00"); 
        dadosParaPdf.put("Endereço", "Endereço da Empresa, 123 - Cidade, Estado - CEP: 00000-000");
        dadosParaPdf.put("periodoReferencia", new SimpleDateFormat("MMMM/yyyy").format(new Date())); 
        dadosParaPdf.put("dataPagamento", new SimpleDateFormat("dd/MM/yyyy").format(new Date())); 
        dadosParaPdf.put("nomeFuncionario", nome);
        dadosParaPdf.put("cpf", cpf);
        dadosParaPdf.put("cargo", cargo);
        dadosParaPdf.put("salarioBase", String.format("%.2f", salarioBruto));
        dadosParaPdf.put("inssValor", String.format("%.2f", inss));
        dadosParaPdf.put("irrfValor", String.format("%.2f", irrf));
        dadosParaPdf.put("valorLiquido", String.format("%.2f", salarioLiquido));
        dadosParaPdf.put("valeTransporte", String.format("%.2f", valeTransporte));
        dadosParaPdf.put("valeRefeicao", String.format("%.2f", valeRefeicao));
        dadosParaPdf.put("planoDeSaude", String.format("%.2f", planoDeSaude));
        dadosParaPdf.put("auxilioCreche", String.format("%.2f", auxilioCreche));
        dadosParaPdf.put("beneficiosCreditados", String.format("%.2f", beneficiosCreditados));
        dadosParaPdf.put("descontosDebitados", String.format("%.2f", descontosDebitados));
        dadosParaPdf.put("fgtsMes", String.format("%.2f", fgtsMes));

        try {
            geradorPDFService.gerarContrachequePdf(dadosParaPdf, nome);
        } catch (IOException e) {
            e.printStackTrace();
        }

        salvarContracheque(funcionarioId, salarioBruto, salarioLiquido, inss, irrf, valeTransporte, valeRefeicao, planoDeSaude, auxilioCreche);
    }

    @Transactional
    public void salvarContracheque(Integer funcionarioId, double salarioBruto, double salarioLiquido, double inss, double irrf, double valeTransporte, double valeRefeicao, double planoDeSaude, double auxilioCreche) {
        Contracheque contracheque = new Contracheque();
        contracheque.setFuncionarioId(funcionarioId);
        contracheque.setSalarioBruto(salarioBruto);
        contracheque.setSalarioLiquido(salarioLiquido);
        contracheque.setInss(inss);
        contracheque.setIrrf(irrf);
        contracheque.setValeTransporte(valeTransporte);
        contracheque.setValeRefeicao(valeRefeicao);
        contracheque.setPlanoDeSaude(planoDeSaude);
        contracheque.setAuxilioCreche(auxilioCreche);
        contracheque.setDataReferencia(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        save(contracheque);
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
    public void save(Contracheque item) {
        entityManager.persist(item);
    }

    public Contracheque findById(Integer id) {
        return entityManager.find(Contracheque.class, id);
    }

    public List<Contracheque> findAll() {
        // create a query. Explanation: TypedQuery is a type-safe query. It is a query that returns a specific type of object.
        TypedQuery<Contracheque> query = entityManager.createQuery("from Contracheque Order By id_funcionario", Contracheque.class);
        return query.getResultList();
    }

    public List<Contracheque> findByIdFuncionario(Integer idFuncionario) {
        TypedQuery<Contracheque> query = entityManager.createQuery("from Contracheque where id_funcionario = :theData", Contracheque.class);
        query.setParameter("theData", idFuncionario);
        return query.getResultList();
    }

    @Transactional
    public void update(Contracheque item) {
        entityManager.merge(item);
    }

    @Transactional
    public void delete(Integer id) {
        Contracheque item = findById(id);
        entityManager.remove(item);
    }

    @Transactional
    public int deleteAll() {
        int rowsDeleted = entityManager.createQuery("Delete from Contracheque").executeUpdate();
        return rowsDeleted;
    }

}