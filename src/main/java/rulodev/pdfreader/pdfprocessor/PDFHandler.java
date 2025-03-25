package rulodev.pdfreader.pdfprocessor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFHandler {

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
                String cleanedLine = TextProcessor.removeSpecialCharacters(line);
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

    public List<List<String>> processPDFFile(File pdfFile, int pageNumber) {
        return processSpecificPage(pdfFile, pageNumber);
    }

}
