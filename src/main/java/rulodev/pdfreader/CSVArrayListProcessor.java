package rulodev.pdfreader;

import java.util.Arrays;
import java.util.List;
import static rulodev.pdfreader.csvprocessor.CSVArrayList.readCSV;
import rulodev.pdfreader.csvprocessor.CsvFinalData;

public class CSVArrayListProcessor {

    public static void main(String[] args) {

        String csvFilePath = "C:\\Users\\strategic\\OneDrive\\Documentos\\consolidated_output.csv";
        String outputFilePath = "C:\\Users\\strategic\\OneDrive\\Documentos\\filtered_output\\FilteredDataInRows.csv";

        // Convertir el CSV en una "matriz" usando ArrayList
        List<List<String>> matrix = readCSV(csvFilePath);

        // Imprimir por consola todo el contenido del CSV
        System.out.println("Contenido del CSV:");
        for (List<String> row : matrix) {
            for (String cell : row) {
                System.out.print(cell + "\t");
            }
            System.out.println();
        }

        List<int[]> positions = Arrays.asList(
                new int[]{1, 0},
                new int[]{7, 0},
                new int[]{7, 2},
                new int[]{7, 3},
                new int[]{4, 11}
        );

        CsvFinalData processor = new CsvFinalData(outputFilePath, outputFilePath);
        processor.processAndWriteCSV(matrix, positions);

    }

}
