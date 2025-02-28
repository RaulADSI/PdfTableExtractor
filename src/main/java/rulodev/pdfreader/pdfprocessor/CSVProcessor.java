
package rulodev.pdfreader.pdfprocessor;

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


public class CSVProcessor {
    
    private String inputDir;
    private String outputDir;
    private String startKeyword;
    private String stopKeyword;

    public CSVProcessor(String inputDir, String outputDir, String startKeyword, String stopKeyword) {
        this.inputDir = inputDir;
        this.outputDir = outputDir;
        this.startKeyword = startKeyword;
        this.stopKeyword = stopKeyword;
    }
    
    public void processFiles() throws CsvException {
        try {
            Files.createDirectories(Paths.get(outputDir));

            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(inputDir), "*.csv");
            for (Path csvFilePath : directoryStream) {
                processCSVFile(csvFilePath.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processCSVFile(String csvFile) throws CsvException {
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
                        return;
                    }
                }
                if (foundStartKeyword) {
                    writer.write(String.join(",", row));
                    writer.newLine();
                }
            }

        } catch (IOException | CsvException e) {
        }
    
    }
}
