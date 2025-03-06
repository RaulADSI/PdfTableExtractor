package rulodev.pdfreader;

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
        int fila = 7;     // Recordemos que el índice inicia en 0
        int columna = 0;
        if (matrix.size() > fila && matrix.get(fila).size() > columna) {
            String elemento = matrix.get(fila).get(columna);
            System.out.println("\nElemento en la fila " + (fila + 1) + ", columna " + (columna + 1) + " : " + elemento);
        } else {
            System.out.println("El índice solicitado excede el rango de datos.");
        }
    }

}
