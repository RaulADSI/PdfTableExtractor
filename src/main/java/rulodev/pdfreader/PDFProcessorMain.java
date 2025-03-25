package rulodev.pdfreader;

import com.opencsv.exceptions.CsvException;
import java.io.File;
import java.util.Scanner;
import rulodev.pdfreader.pdfprocessor.PDFProcessor;

/**
 *
 * @author Raul_Torres
 */
public class PDFProcessorMain {

    public static void main(String[] args) throws CsvException {
        // Directorio de entrada y ruta del archivo CSV de salida
        String pdfInputDir = "C:\\Users\\strategic\\OneDrive\\Documentos\\Rentify\\930 WareHouse\\FLP";
        String consolidatedOutputFile = "C:\\Users\\strategic\\OneDrive\\Documentos\\consolidated_output2.csv";

        PDFProcessor pdfProcessor = new PDFProcessor(pdfInputDir, consolidatedOutputFile);
        System.out.println("Selecciona una opción:");
        System.out.println("1. Procesar todos los archivos PDF usando expresiones regulares.");
        System.out.println("2. Procesar una página específica de un archivo PDF.");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.println("Extrayendo información de archivos PDF usando expresiones regulares...");
                // Procesa todos los PDFs del directorio extrayendo montos, fechas y números de cuenta
                pdfProcessor.processDirectoryByRegex();
                System.out.println("Proceso completado. Revisa el archivo: " + consolidatedOutputFile);
                break;

            case 2:
                scanner.nextLine(); // Limpiar el buffer
                System.out.println("Introduce la ruta completa del archivo PDF:");
                String pdfFilePath = scanner.nextLine();

                System.out.println("Introduce el número de página que deseas procesar (0 para todas):");
                int pageNumber = scanner.nextInt();

                try {
                    File pdfFile = new File(pdfFilePath);
                    System.out.println("Procesando página " + pageNumber + " del archivo: " + pdfFilePath);

                    var table = pdfProcessor.processSpecificPage(pdfFile, pageNumber);

                    // Imprimir el contenido de la página procesada
                    System.out.println("Contenido procesado:");
                    for (var row : table) {
                        System.out.println(String.join(", ", row));
                    }

                    // Guardar en el archivo de salida
                    pdfProcessor.saveToCSV(table, consolidatedOutputFile);
                    System.out.println("Resultados guardados en: " + consolidatedOutputFile);
                } catch (Exception e) {
                    System.err.println("Ocurrió un error al procesar el archivo: " + e.getMessage());
                }
                break;

            default:
                System.out.println("Opción no válida. Terminando el programa.");
        }

        scanner.close();
                
    }

}
