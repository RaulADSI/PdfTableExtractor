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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    public List<List<String>> extractDataUsingRegex(File pdfFile) {
        List<List<String>> extractedData = new ArrayList<>();
        try {
            PDDocument document = PDDocument.load(pdfFile);
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            document.close();

            // --- Definir patrones de expresiones regulares ---
            // Patrón para montos:
            // Ejemplo: 1,234.56 o 1234.56 o simplemente 1234
            Pattern amountPattern = Pattern.compile("\\b\\d{1,3}(,\\d{3})*(\\.\\d{2})?\\b");
            // Patrón para fechas:
            // Ejemplo: 12/05/2023 o 3-4-21
            Pattern datePattern = Pattern.compile("\\b\\d{1,2}[/-]\\d{1,2}[/-]\\d{2,4}\\b");
            // Patrón para números de cuenta (suponiendo entre 8 y 20 dígitos consecutivos):
            Pattern accountPattern = Pattern.compile("\\b\\d{8,20}\\b");

            // --- Buscar coincidencias ---
            List<String> amounts = new ArrayList<>();
            Matcher amountMatcher = amountPattern.matcher(text);
            while (amountMatcher.find()) {
                amounts.add(amountMatcher.group());
            }

            List<String> dates = new ArrayList<>();
            Matcher dateMatcher = datePattern.matcher(text);
            while (dateMatcher.find()) {
                dates.add(dateMatcher.group());
            }

            List<String> accountNumbers = new ArrayList<>();
            Matcher accountMatcher = accountPattern.matcher(text);
            while (accountMatcher.find()) {
                accountNumbers.add(accountMatcher.group());
            }

            // Agregar los resultados a la lista general
            extractedData.add(amounts);
            extractedData.add(dates);
            extractedData.add(accountNumbers);

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

    public void processDirectoryByRegex() {
        List<List<String>> consolidatedData = new ArrayList<>();
        try {
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(inputDir), "*.pdf");
            for (Path pdfFilePath : directoryStream) {
                File pdfFile = pdfFilePath.toFile();
                System.out.println("Procesando archivo: " + pdfFile.getName());
                List<List<String>> extracted = extractDataUsingRegex(pdfFile);
                // Consolidamos los datos. Por ejemplo, puedes agregar cada sublista en consolidatedData.
                // Aquí simplemente agregamos todas las listas extraídas.
                consolidatedData.addAll(extracted);
            }
            saveToCSV(consolidatedData, outputFilePath);
            System.out.println("Archivos procesados y resultados guardados en: " + outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
