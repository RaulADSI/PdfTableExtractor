package rulodev.pdfreader;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import static rulodev.pdfreader.pdfprocessor.CSVArrayList.readCSV;

public class CSVArrayListProcessor {

    public static void main(String[] args) {

        String csvFilePath = "C:\\Users\\strategic\\OneDrive\\Documentos\\filtered_output\\Wingate water 12324404 Sept - Oct 2023_filtered.csv";

        // Convertir el CSV en una "matriz" usando ArrayList
        List<List<String>> matrix = readCSV(csvFilePath);

        // Imprimir todo el contenido del CSV
        System.out.println("Contenido del CSV:");
        for (List<String> row : matrix) {
            for (String cell : row) {
                System.out.print(cell + "\t");
            }
            System.out.println();
        }

        //Acceder a un elemento específico (fila 1, columna 0)
//        int fila = 7;     // Recordemos que el índice inicia en 0
//        int columna = 0;
//        if (matrix.size() > fila && matrix.get(fila).size() > columna) {
//            String elemento = matrix.get(fila).get(columna);
//            System.out.println("\nElemento en la fila " + (fila + 1) + ", columna " + (columna + 1) + " : " + elemento);
//        } else {
//            System.out.println("El índice solicitado excede el rango de datos.");
//        }
       List<int[]> positions = Arrays.asList(
            new int[]{1, 0},  
            new int[]{7, 0},  
            new int[]{7, 2},
            new int[]{7, 3},
            new int[]{4, 11}
        );

        String outputFilePath = "C:\\Users\\strategic\\OneDrive\\Documentos\\filtered_output\\FilteredData.csv";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            writer.write("Account,Invoice Date,Service from,Service to, Amount"); // Escribir encabezados
            writer.newLine();

             for (int[] position : positions) {
                int fila = position[0];
                int columna = position[1];

                if (fila < matrix.size() && columna < matrix.get(fila).size()) {
                    String valor = matrix.get(fila).get(columna);
                    writer.write(valor + ","); // Separar valores por comas
                } else {
                    writer.write("Fuera de Rango,"); // Manejar excepciones
                }
            }
            System.out.println("¡Datos guardados en el archivo CSV en: " + outputFilePath);
        } catch (IOException e) {
            System.err.println("Ocurrió un error al escribir el archivo CSV: " + e.getMessage());
        }
    }

}
