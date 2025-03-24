package rulodev.pdfreader;

import com.opencsv.exceptions.CsvException;
import rulodev.pdfreader.pdfprocessor.PDFProcessor;

/**
 *
 * @author Raul_Torres
 */
public class PDFProcessorMain {

    public static void main(String[] args) throws CsvException {
        // Directorio de entrada y ruta del archivo CSV de salida
        String pdfInputDir = "C:\\Users\\strategic\\OneDrive\\Documentos\\Rentify\\930 WareHouse\\FLP";
        String consolidatedOutputFile = "C:\\Users\\strategic\\OneDrive\\Documentos\\consolidated_output.csv";
        
        // palabras claves para iniciar y detener la extracción
        String startKeyword = "CURRENT BILL";
        String stopKeyword = "1-800-4OUTAGE";

        PDFProcessor pdfProcessor = new PDFProcessor(pdfInputDir, consolidatedOutputFile);
        System.out.println("Extrayendo información de los archivos PDF usando palabras claves...");
        
        // Procesa todos los PDFs del directorio basándose en las palabras claves definidas.
        pdfProcessor.processDirectoryByKeywords(startKeyword, stopKeyword);
        
        System.out.println("Información extraída y guardada en: " + consolidatedOutputFile);

        }
}
