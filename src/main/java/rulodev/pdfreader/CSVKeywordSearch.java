package rulodev.pdfreader;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 * @author Raul_Torres
 */
public class CSVKeywordSearch {

    public static void main(String[] args) {
        
        String inputDir = "C:\\Users\\strategic\\OneDrive\\Documentos\\output\\"; // Directorio con los archivos CSV
        String outputDir = "C:\\Users\\strategic\\OneDrive\\Documentos\\filtered_output\\"; // Directorio de salida
        String startKeyword = "Account"; // Palabra clave inicial
        String stopKeyword = "PAYMENT"; // Palabra clave final
        

        try {
            Files.createDirectories(Paths.get(outputDir));
            
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(inputDir), "*.csv");
            for (Path csvFilePath : directoryStream) {
                processCSVFile(csvFilePath.toString(), outputDir, startKeyword, stopKeyword);
            }
        } catch (IOException e) {
        }
    }
    
    private static void processCSVFile(String csvFile, String outputDir, String startKeyword, String stopKeyword) {
        boolean foundStartKeyword = false;
        String newCsvFile = outputDir + Paths.get(csvFile).getFileName().toString().replace(".csv", "_filtered.csv");

        try (CSVReader reader = new CSVReader(new FileReader(csvFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(newCsvFile))) {
            List<String[]> allRows = reader.readAll();

            for (String[] row : allRows) {
                for (String cell : row) {
                    if (cell.contains(startKeyword)) {
                        foundStartKeyword = true;
                    }
                    if (cell.contains(stopKeyword)) {
                        System.out.println("Palabra clave de fin encontrada en " + csvFile + ". Deteniendo ejecución.");
                        return; // Detener la ejecución del programa
                    }
                }
                if (foundStartKeyword) {
                    // Escribir la fila actual y las siguientes en el nuevo archivo
                    writer.write(String.join(",", row));
                    writer.newLine();
                }
            }
            
        } catch (IOException | CsvException e) {
        }
    }

}
