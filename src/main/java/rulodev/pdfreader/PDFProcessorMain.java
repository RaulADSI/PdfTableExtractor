package rulodev.pdfreader;

import com.opencsv.exceptions.CsvException;
import rulodev.pdfreader.pdfprocessor.CSVProcessor;
import rulodev.pdfreader.pdfprocessor.PDFProcessor;

/**
 *
 * @author Raul_Torres
 */
public class PDFProcessorMain {

    public static void main(String[] args) throws CsvException {
        String pdfInputDir = "C:\\Users\\strategic\\OneDrive\\Documentos\\Mr Darsh\\";
        String pdfOutputDir = "C:\\Users\\strategic\\OneDrive\\Documentos\\output\\";
        String csvInputDir = "C:\\Users\\strategic\\OneDrive\\Documentos\\output\\";
        String csvOutputDir = "C:\\Users\\strategic\\OneDrive\\Documentos\\filtered_output\\";
        String startKeyword = "Account";
        String stopKeyword = "PAYMENT";

        PDFProcessor pdfProcessor = new PDFProcessor(pdfInputDir, pdfOutputDir);
        CSVProcessor csvProcessor = new CSVProcessor(csvInputDir, csvOutputDir);

        System.out.println("Procesando archivos PDF...");
        pdfProcessor.processFiles();

        System.out.println("Procesando archivos CSV...");
        csvProcessor.processFiles();
    }
}
