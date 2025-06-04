package com.projectpayroll.payroll.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.projectpayroll.payroll.dao.FuncionarioBeneficioRepository;
import com.projectpayroll.payroll.dao.FuncionarioRepository;
import com.projectpayroll.payroll.entity.Beneficios;
import com.projectpayroll.payroll.entity.FuncionarioBeneficio;

@Component
public class CalculadoraSalario {

    private FuncionarioRepository funcionarioRepository;
    private FuncionarioBeneficioRepository funcionarioBeneficioRepository;

    @Autowired
    public CalculadoraSalario(FuncionarioRepository funcionarioRepository, FuncionarioBeneficioRepository funcionarioBeneficioRepository) {
        this.funcionarioRepository = funcionarioRepository;
        this.funcionarioBeneficioRepository = funcionarioBeneficioRepository;
    }

        // INSS
        // Faixa 1
        private static final double FAIXA_INSS_1_LIMITE = 1518.00; 
        private static final double FAIXA_INSS_1_ALIQUOTA = 0.075; 
    
        // Faixa 2
        private static final double FAIXA_INSS_2_LIMITE =  2793.88; 
        private static final double FAIXA_INSS_2_ALIQUOTA = 0.09; 
    
        // Faixa 3
        private static final double FAIXA_INSS_3_LIMITE = 4190.83; 
        private static final double FAIXA_INSS_3_ALIQUOTA = 0.12;  
    
        // Faixa 4
        private static final double FAIXA_INSS_4_LIMITE = 8157.41; 
        private static final double FAIXA_INSS_4_ALIQUOTA = 0.14;  


        // IRRF 
        // Faixa 1
        private static final double BASE_IRRF_FAIXA1_LIMITE = 2528.80; 
        // Faixa 2
        private static final double BASE_IRRF_FAIXA2_LIMITE = 2826.65; 
        private static final double BASE_IRRF_FAIXA2_ALIQUOTA = 0.075;
        private static final double BASE_IRRF_FAIXA2_DEDUCAO = 169.44;  

        // Faixa 3
        private static final double BASE_IRRF_FAIXA3_LIMITE = 3751.05; 
        private static final double BASE_IRRF_FAIXA3_ALIQUOTA = 0.15;  
        private static final double BASE_IRRF_FAIXA3_DEDUCAO = 381.44;  

        // Faixa 4
        private static final double BASE_IRRF_FAIXA4_LIMITE = 4664.68; 
        private static final double BASE_IRRF_FAIXA4_ALIQUOTA = 0.225; 
        private static final double BASE_IRRF_FAIXA4_DEDUCAO = 662.77; 

        // Faixa 5 (Acima do limite da Faixa 4)
        private static final double BASE_IRRF_FAIXA5_ALIQUOTA = 0.275; 
        private static final double BASE_IRRF_FAIXA5_DEDUCAO = 896.00;  
    

        // DEDUÇÃO INSS
        public double calcularINSS(double salarioBruto) {
            double inssCalculado = 0.0;
    
            // Garante que o salário bruto não exceda o teto para cálculo progressivo
            double salarioContribuicao = Math.min(salarioBruto, FAIXA_INSS_4_LIMITE);
    
            // Faixa 1
            if (salarioContribuicao <= FAIXA_INSS_1_LIMITE) {
                inssCalculado = salarioContribuicao * FAIXA_INSS_1_ALIQUOTA;
            } else {
                inssCalculado = FAIXA_INSS_1_LIMITE * FAIXA_INSS_1_ALIQUOTA;
    
                // Faixa 2
                if (salarioContribuicao <= FAIXA_INSS_2_LIMITE) {
                    inssCalculado += (salarioContribuicao - FAIXA_INSS_1_LIMITE) * FAIXA_INSS_2_ALIQUOTA;
                } else {
                    inssCalculado += (FAIXA_INSS_2_LIMITE - FAIXA_INSS_1_LIMITE) * FAIXA_INSS_2_ALIQUOTA;
    
                    // Faixa 3
                    if (salarioContribuicao <= FAIXA_INSS_3_LIMITE) {
                        inssCalculado += (salarioContribuicao - FAIXA_INSS_2_LIMITE) * FAIXA_INSS_3_ALIQUOTA;
                    } else {
                        inssCalculado += (FAIXA_INSS_3_LIMITE - FAIXA_INSS_2_LIMITE) * FAIXA_INSS_3_ALIQUOTA;
    
                        // Faixa 4
                        inssCalculado += (salarioContribuicao - FAIXA_INSS_3_LIMITE) * FAIXA_INSS_4_ALIQUOTA;
                    }
                }
            }
            return Math.round(inssCalculado * 100.0) / 100.0;
        }



        //Dedução IRRF
        public double calcularIRRF(double salarioBruto, double valorINSS) {
            double baseCalculoIRRF = salarioBruto - valorINSS;
    
            double irrfCalculado = 0.0;
    
            
            if (baseCalculoIRRF <= BASE_IRRF_FAIXA1_LIMITE) {
                irrfCalculado = 0.0;
            } else if (baseCalculoIRRF <= BASE_IRRF_FAIXA2_LIMITE) {
                irrfCalculado = (baseCalculoIRRF * BASE_IRRF_FAIXA2_ALIQUOTA) - BASE_IRRF_FAIXA2_DEDUCAO;
            } else if (baseCalculoIRRF <= BASE_IRRF_FAIXA3_LIMITE) {
                irrfCalculado = (baseCalculoIRRF * BASE_IRRF_FAIXA3_ALIQUOTA) - BASE_IRRF_FAIXA3_DEDUCAO;
            } else if (baseCalculoIRRF <= BASE_IRRF_FAIXA4_LIMITE) {
                irrfCalculado = (baseCalculoIRRF * BASE_IRRF_FAIXA4_ALIQUOTA) - BASE_IRRF_FAIXA4_DEDUCAO;
            } else {
                irrfCalculado = (baseCalculoIRRF * BASE_IRRF_FAIXA5_ALIQUOTA) - BASE_IRRF_FAIXA5_DEDUCAO;
            }
    
            if (irrfCalculado < 0) {
                irrfCalculado = 0.0;
            }

            return Math.round(irrfCalculado * 100.0) / 100.0;
        }

        public double getValeTransporteDeFuncionario(Integer funcionarioId) {
            List<FuncionarioBeneficio> beneficiosFuncionario = funcionarioBeneficioRepository.findByFuncionario_Id(funcionarioId);
        for (FuncionarioBeneficio item : beneficiosFuncionario) {
            if (item.getBeneficio().getName().equalsIgnoreCase("Vale-Transporte")) {
                return item.getBeneficio().getDefaultValue();
            }
        }
        return 0;
    }






        public double getValeRefeicaoDeFuncionario(Integer funcionarioId) {
            List<FuncionarioBeneficio> beneficiosFuncionario = funcionarioBeneficioRepository.findByFuncionario_Id(funcionarioId);
        for (FuncionarioBeneficio item : beneficiosFuncionario) {
            if (item.getBeneficio().getName().equalsIgnoreCase("Vale-Refeicao")) {
                return item.getBeneficio().getDefaultValue();
            }
        }
        return 0;
    }

        public double getPlanoDeSaudeDeFuncionario(Integer funcionarioId) {
            List<FuncionarioBeneficio> beneficiosFuncionario = funcionarioBeneficioRepository.findByFuncionario_Id(funcionarioId);
        for (FuncionarioBeneficio item : beneficiosFuncionario) {
            if (item.getBeneficio().getName().equalsIgnoreCase("Plano de Saude")) {
                return item.getBeneficio().getDefaultValue();
            }
        }
        return 0;
    }

        public double getAuxilioCrecheDeFuncionario(Integer funcionarioId) {
            List<FuncionarioBeneficio> beneficiosFuncionario = funcionarioBeneficioRepository.findByFuncionario_Id(funcionarioId);
        for (FuncionarioBeneficio item : beneficiosFuncionario) {
            if (item.getBeneficio().getName().equalsIgnoreCase("Auxílio Creche")) {
                return item.getBeneficio().getDefaultValue();
            }
        }
        return 0;
    }

    
    }

