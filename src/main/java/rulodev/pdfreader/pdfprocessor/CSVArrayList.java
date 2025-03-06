package rulodev.pdfreader.pdfprocessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVArrayList {

    public static List<List<String>> readCSV(String filePath) {
        List<List<String>> matrix = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Procesa el archivo línea por línea
            while ((line = br.readLine()) != null) {
                // Separa la línea en celdas usando la coma como separador
                String[] cells = line.split(",");
                List<String> row = new ArrayList<>();

                for (String cell : cells) {
                    // Se realiza un "trim" para limpiar espacios adicionales
                    row.add(cell.trim());
                }

                // Agregar la fila (como ArrayList) a la "matriz"
                matrix.add(row);
            }

        } catch (IOException e) {
        }

        return matrix;

    }
}
