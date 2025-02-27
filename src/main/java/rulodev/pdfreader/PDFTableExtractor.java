
package rulodev.pdfreader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;


public class PDFTableExtractor {
    public static void main(String[] args) {
       try {
            File pdfFile = new File("C:\\Users\\strategic\\OneDrive\\Documentos\\Mr Darsh\\Wingate water 12324404 Sept - Oct 2023.pdf");
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
            
            //saveToCSV(table, "C:\\Users\\strategic\\OneDrive\\Documentos\\output.csv");

            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void saveToCSV(List<List<String>> table, String filePath) {
    try {
        File outputFile = new File(filePath);
        outputFile.getParentFile().mkdirs();  // Crear directorios si no existen

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (List<String> row : table) {
                writer.write(String.join(",", row));
                writer.newLine();
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

}
