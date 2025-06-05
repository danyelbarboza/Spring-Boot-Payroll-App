package com.projectpayroll.payroll.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;

/**
 * Converts a simple HTML file to PDF using an InputStream and a PdfDocument
 * as arguments for the convertToPdf() method.
 */
public class GeradorPDFService {

    /**
     * The path to the resulting PDF file.
     */
    public static final String DEST = "./target/htmlsamples/ch01/ContraCheque.pdf";

    /**
     * The Base URI of the HTML page.
     */
    public static final String BASEURI = "./src/main/resources/templates/";

    /**
     * The path to the source HTML file.
     */
    public static final String SRC = String.format("%sContraCheque.html", BASEURI);

    /**
     * The main method of this example.
     *
     * @param args no arguments are needed to run this example.
     * @throws IOException signals that an I/O exception has occurred.
     */
    public static void iniciarGeracaoPdf() throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new GeradorPDFService().criarPdf(BASEURI, SRC, DEST);
    }

    /**
     * Creates the PDF file.
     *
     * @param baseUri the base URI
     * @param src     the path to the source HTML file
     * @param dest    the path to the resulting PDF
     * @throws IOException signals that an I/O exception has occurred.
     */
    public void criarPdf(String baseUri, String src, String dest) throws IOException { 
    	ConverterProperties properties = new ConverterProperties();
    	properties.setBaseUri(baseUri);
    	PdfWriter writer = new PdfWriter(dest);
    	PdfDocument pdf = new PdfDocument(writer);
    	pdf.setTagged();
    	pdf.setDefaultPageSize(new PageSize(595, 14400));
    	HtmlConverter.convertToPdf(new FileInputStream(src), pdf, properties);
    }
}
