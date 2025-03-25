package rulodev.pdfreader;

import com.opencsv.exceptions.CsvException;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import rulodev.pdfreader.pdfprocessor.FileWriterHelper;
import rulodev.pdfreader.pdfprocessor.PDFHandler;
import rulodev.pdfreader.pdfprocessor.PDFProcessor;

/**
 *
 * @author Raul_Torres
 */
public class PDFProcessorMain {

    public static void main(String[] args) throws CsvException, IOException {
        
        // Directorio de entrada y ruta del archivo CSV de salida
        String pdfInputDir = "C:\\Users\\strategic\\OneDrive\\Documentos\\Rentify\\930 WareHouse\\FLP";
        String consolidatedOutputFile = "C:\\Users\\strategic\\OneDrive\\Documentos\\consolidated_output2.csv";

        PDFProcessor pdfProcessor = new PDFProcessor(pdfInputDir, consolidatedOutputFile);
        PDFHandler pdfHandler = new PDFHandler();
        
        System.out.println("Selecciona una opción:");
        System.out.println("1. Procesar todos los archivos PDF usando expresiones regulares.");
        System.out.println("2. Procesar una página específica de un archivo PDF.");

        try (Scanner scanner = new Scanner(System.in)) {
            int choice = scanner.nextInt();
            
            switch (choice) {
                case 1 -> {
                    System.out.println("Extrayendo información de archivos PDF usando expresiones regulares...");
                    // Procesa todos los PDFs del directorio extrayendo montos, fechas y números de cuenta
                    pdfProcessor.processDirectoryByRegex();
                    System.out.println("Proceso completado. Revisa el archivo: " + consolidatedOutputFile);
                }
                    
                case 2 -> {
                    scanner.nextLine(); // Limpiar el buffer
                    System.out.println("Introduce la ruta completa del archivo PDF:");
                    String pdfFilePath = scanner.nextLine();
                    
                    System.out.println("Introduce el numero de pagina que deseas procesar (0 para todas):");
                    int pageNumber = scanner.nextInt();
                    
                    try {
                        File pdfFile = new File(pdfFilePath);
                        System.out.println("Procesando pagina " + pageNumber + " del archivo: " + pdfFilePath);
                        
                        var table = pdfHandler.processSpecificPage(pdfFile, pageNumber);
                        
                        // Imprimir el contenido de la página procesada
                        System.out.println("Contenido procesado:");
                        for (var row : table) {
                            System.out.println(String.join(", ", row));
                        }
                        
                        // Guardar en el archivo de salida
                        FileWriterHelper.saveToCSV(table, consolidatedOutputFile);
                        System.out.println("Resultados guardados en: " + consolidatedOutputFile);
                    } catch (Exception e) {
                        System.err.println("Ocurrio un error al procesar el archivo: " + e.getMessage());
                    }
                }
                    
                default -> System.out.println("Opción no válida. Terminando el programa.");
            }
        }
                
    }

}
