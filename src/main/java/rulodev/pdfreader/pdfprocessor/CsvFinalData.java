
package rulodev.pdfreader.pdfprocessor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class CsvFinalData {
    private String inputFilePath;
    private String outputFilePath;

    // Constructor para inicializar las rutas de entrada y salida
    public CsvFinalData(String inputFilePath, String outputFilePath) {
        this.inputFilePath = inputFilePath;
        this.outputFilePath = outputFilePath;
    }

    // Método para procesar el archivo CSV y escribir los datos filtrados
    public void processAndWriteCSV(List<List<String>> matrix, List<int[]> positions) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            writer.write("Number Account,Invoice date, Service From, Service To, Amount"); // Encabezado opcional
            writer.newLine();

            for (int[] position : positions) {
                int fila = position[0];
                int columna = position[1];

                if (fila < matrix.size() && columna < matrix.get(fila).size()) {
                    String valor = matrix.get(fila).get(columna);
                    writer.write(valor + ","); // Escribir valor separado por comas
                } else {
                    writer.write("Fuera de Rango,"); // Manejar datos fuera de rango
                }
            }
            System.out.println("¡Datos guardados en formato de fila única en: " + outputFilePath);
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo CSV: " + e.getMessage());
        }
    }
}
