package rulodev.pdfreader.pdfprocessor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFProcessor {

    private String inputDir;
    private String outputFilePath;

    public PDFProcessor(String inputDir, String outputFilePath) {
        this.inputDir = inputDir;
        this.outputFilePath = outputFilePath;
    }

    private String removeSpecialCharacters(String input) {
        if (input == null) {
            return ""; // Devuelve una cadena vacía si la entrada es null.
        }
        return input.replaceAll("[^a-zA-Z0-9\\s.]", ""); // Elimina todo excepto letras, números y espacios.
    }

    public List<List<String>> extractInformationUsingKeywords(File pdfFile, String startKeyword, String stopKeyword) {
        List<List<String>> extractedData = new ArrayList<>();
        try {
            PDDocument document = PDDocument.load(pdfFile);
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            String[] lines = text.split("\\r?\\n");

            boolean capture = false;
            for (String line : lines) {
                // Si encontramos la palabra clave de inicio, empezamos a capturar
                if (line.contains(startKeyword)) {
                    capture = true;
                }

                if (capture) {
                    // Procesamos la línea: quitamos caracteres especiales y separamos por espacios
                    String cleanedLine = removeSpecialCharacters(line);
                    // Evitamos agregar líneas vacías
                    if (!cleanedLine.trim().isEmpty()) {
                        List<String> row = Arrays.asList(cleanedLine.split("\\s+"));
                        extractedData.add(row);
                    }
                }

                // Si encontramos la palabra clave de fin, detenemos la captura
                if (line.contains(stopKeyword)) {
                    capture = false;
                }
            }
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return extractedData;
    }

    public void processDirectory(int pageNumber) throws IOException {

        List<List<String>> consolidatedTable = new ArrayList<>();
        // Crear un flujo para recorrer los archivos del directorio con extensión .pdf
        DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(inputDir), "*.pdf");
        for (Path pdfFilePath : directoryStream) {
            File pdfFile = pdfFilePath.toFile();
            System.out.println("Procesando archivo: " + pdfFile.getName());

            // Procesar el archivo, ya sea una página específica o el documento completo
            List<List<String>> table = processPDFFile(pdfFile, pageNumber);
            consolidatedTable.addAll(table);
        }
        // Guardar los resultados consolidados en un archivo CSV
        saveToCSV(consolidatedTable, outputFilePath);
        System.out.println("Archivos procesados y resultados guardados en: " + outputFilePath);

    }

    public List<List<String>> processSpecificPage(File pdfFile, int pageNumber) {

        List<List<String>> table = new ArrayList<>();
        try {
            PDDocument document = PDDocument.load(pdfFile);
            PDFTextStripper pdfStripper = new PDFTextStripper();

            if (pageNumber > 0) {
                // Procesa solo la página especificada
                pdfStripper.setStartPage(pageNumber);
                pdfStripper.setEndPage(pageNumber);
            }
            // Si pageNumber es 0 o negativo, no se establecen límites y se procesa el documento completo.

            String text = pdfStripper.getText(document);
            String[] lines = text.split("\\r?\\n");

            for (String line : lines) {
                String cleanedLine = removeSpecialCharacters(line);
                // Verificar que la línea no esté vacía tras la limpieza.
                if (!cleanedLine.trim().isEmpty()) {
                    String[] columns = cleanedLine.split("\\s+");
                    List<String> row = new ArrayList<>();
                    for (String column : columns) {
                        row.add(column);
                    }
                    table.add(row);
                }
            }
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return table;

    }

    private List<List<String>> processPDFFile(File pdfFile, int pageNumber) {
        return processSpecificPage(pdfFile, pageNumber);
    }

    public void saveToCSV(List<List<String>> table, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (List<String> row : table) {
                writer.write(String.join(",", row));
                writer.newLine();
            }
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processDirectoryByKeywords(String startKeyword, String stopKeyword) {
        List<List<String>> consolidatedTable = new ArrayList<>();
        try {
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(inputDir), "*.pdf");
            for (Path pdfFilePath : directoryStream) {
                File pdfFile = pdfFilePath.toFile();
                System.out.println("Procesando archivo: " + pdfFile.getName());
                List<List<String>> extractedData = extractInformationUsingKeywords(pdfFile, startKeyword, stopKeyword);
                consolidatedTable.addAll(extractedData);
            }
            saveToCSV(consolidatedTable, outputFilePath);
            System.out.println("Archivos procesados y resultados guardados en: " + outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
