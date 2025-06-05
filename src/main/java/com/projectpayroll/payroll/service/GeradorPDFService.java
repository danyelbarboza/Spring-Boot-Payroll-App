package com.projectpayroll.payroll.service;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.DocumentProperties;
import com.itextpdf.kernel.pdf.WriterProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.ConverterProperties;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service 
public class GeradorPDFService {

    public static String DEST = "./target/htmlsamples/ch01/Contracheque";
    public static String BASEURI = "./src/main/resources/templates/";
    public static final String TEMPLATE_NAME = "Contracheque";

    private final TemplateEngine templateEngine;

    @Autowired
    public GeradorPDFService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    /**
     * Método principal para iniciar a geração do PDF a partir dos dados fornecidos.
     * Este método será chamado pela ContraChequeService.
     *
     * @param dados Mapa contendo as variáveis a serem usadas no template Thymeleaf.
     * @throws IOException Se ocorrer um erro de I/O durante a criação do PDF.
     */
    public void gerarContrachequePdf(Map<String, Object> dados, String nome) throws IOException {
        String DEST = GeradorPDFService.DEST + " de " + nome + " " + System.currentTimeMillis() + ".pdf";
        File file = new File(DEST);
        // Cria diretórios pais se não existirem
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        criarPdfComThymeleaf(TEMPLATE_NAME, DEST, dados);
    }

    /**
     * Cria o arquivo PDF processando um template Thymeleaf com os dados fornecidos.
     *
     * @param templateName O nome do template Thymeleaf (ex: "Contracheque").
     * @param dest         O caminho onde o PDF resultante será salvo.
     * @param dados        Um mapa com os dados para preencher o template.
     * @throws IOException Se ocorrer um erro de I/O.
     */
    public void criarPdfComThymeleaf(String templateName, String dest, Map<String, Object> dados) throws IOException {
        // 1. Criar o contexto do Thymeleaf e adicionar os dados
        Context context = new Context();
        context.setVariables(dados);

        // 2. Processar o template Thymeleaf para gerar o HTML como String
        String htmlContent = templateEngine.process(templateName, context);

        // 3. Configurar propriedades para a conversão HTML para PDF
        ConverterProperties properties = new ConverterProperties();
        properties.setBaseUri(BASEURI); 

        // 4. Criar o escritor e o documento PDF
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);
        pdf.setTagged(); // Para PDFs acessíveis 

        
        pdf.setDefaultPageSize(new PageSize(750, 1360));

        // 5. Converter o HTML (em String) para PDF
        HtmlConverter.convertToPdf(new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8)), pdf, properties);

        pdf.close();
    }
}