
package rulodev.pdfreader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
/**
 *
 * @author Raul_Torres
 */
public class PDFTableExtractor2 {
    
    public static void main(String[] args) {
        String inputDir = "C:\\Users\\strategic\\OneDrive\\Documentos\\Mr Darsh\\"; // Directorio con los archivos PDF
        String outputDir = "C:\\Users\\strategic\\OneDrive\\Documentos\\output\\"; // Directorio de salida

        try {
            Files.createDirectories(Paths.get(outputDir)); // Crear el directorio de salida si no existe

            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(inputDir), "*.pdf");
            for (Path pdfFilePath : directoryStream) {
                processPDFFile(pdfFilePath.toFile(), outputDir);
            }
        } catch (IOException e) {
        }
    }

    private static void processPDFFile(File pdfFile, String outputDir) {
        try {
            PDDocument document = PDDocument.load(pdfFile);

            // Usar PDFTextStripper para extraer texto
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            String[] lines = text.split("\\r?\\n");

            // Extraer tablas (esto dependerá del formato del PDF)
            List<List<String>> table = new ArrayList<>();
            for (String line : lines) {
                String[] columns = line.split("\\s+");
                List<String> row = new ArrayList<>();
                for (String column : columns) {
                    row.add(column);
                }
                table.add(row);
            }

            // Imprimir la tabla (puedes modificar esto para almacenar la tabla en un DataFrame)
            for (List<String> row : table) {
                System.out.println(row);
            }

            // Guardar la tabla en un archivo CSV
            String outputFilePath = outputDir + pdfFile.getName().replace(".pdf", ".csv");
            saveToCSV(table, outputFilePath);

            document.close();
        } catch (IOException e) {
        }
    }

    private static void saveToCSV(List<List<String>> table, String filePath) {
        try {
            File outputFile = new File(filePath);
            outputFile.getParentFile().mkdirs(); // Crear directorios si no existen

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                for (List<String> row : table) {
                    writer.write(String.join(",", row));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
        }
    }
}
