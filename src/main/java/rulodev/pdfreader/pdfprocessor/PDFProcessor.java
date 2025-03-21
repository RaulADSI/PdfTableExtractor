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


    public void processFiles() {
        List<List<String>> consolidatedTable = new ArrayList<>();
        try {
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(inputDir), "*.pdf");
            for (Path pdfFilePath : directoryStream) {
                List<List<String>> table = processPDFFile(pdfFilePath.toFile());
                consolidatedTable.addAll(table);
            }
            saveToCSV(consolidatedTable, outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<List<String>> processPDFFile(File pdfFile) {
        List<List<String>> table = new ArrayList<>();
        try {
            PDDocument document = PDDocument.load(pdfFile);
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            String[] lines = text.split("\\r?\\n");

            for (String line : lines) {
                String cleanedLine = removeSpecialCharacters(line);
                String[] columns = cleanedLine.split("\\s+");
                List<String> row = new ArrayList<>();
                for (String column : columns) {
                    row.add(column);
                }
                table.add(row);
            }

            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return table;
    }

    private void saveToCSV(List<List<String>> table, String filePath) {
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
}
