package rulodev.pdfreader;

import com.opencsv.exceptions.CsvException;
import rulodev.pdfreader.pdfprocessor.PDFProcessor;

/**
 *
 * @author Raul_Torres
 */
public class PDFProcessorMain {

    public static void main(String[] args) throws CsvException {
        String pdfInputDir = "C:\\Users\\strategic\\OneDrive\\Documentos\\Rentify\\930 WareHouse\\FLP";
        String consolidatedOutputFile = "C:\\Users\\strategic\\OneDrive\\Documentos\\consolidated_output1.csv";
        String startKeyword = "CURRENT BILL";
        String stopKeyword = "1-800-4OUTAGE";

        PDFProcessor pdfProcessor = new PDFProcessor(pdfInputDir, consolidatedOutputFile);

        System.out.println("Procesando archivos PDF...");
        pdfProcessor.processFiles();

    }
}
