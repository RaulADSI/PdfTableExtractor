package rulodev.pdfreader.pdfprocessor;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import rulodev.pdfreader.interfaces.FileProcessor;

public class PDFProcessor {

    private String inputDir;
    private String outputFilePath;

    public PDFProcessor(String inputDir, String outputFilePath) {
        this.inputDir = inputDir;
        this.outputFilePath = outputFilePath;
    }

    public void processDirectory(int pageNumber) throws IOException {

        PDFHandler pdfHandler = new PDFHandler();
        List<List<String>> consolidatedTable = new ArrayList<>();

        iterateOverDirectory(inputDir, "pdf", (file) -> {
            List<List<String>> table = pdfHandler.processSpecificPage(file, pageNumber);
            consolidatedTable.addAll(table);
        });

        // Guardar los resultados en un archivo CSV
        FileWriterHelper.saveToCSV(consolidatedTable, outputFilePath);
        System.out.println("Archivos procesados y resultados guardados en: " + outputFilePath);

    }

    public void processDirectoryByRegex() throws IOException {
    DataExtractor pdfHandler = new DataExtractor();
    List<List<String>> consolidatedData = new ArrayList<>();

    iterateOverDirectory(inputDir, "pdf", (file) -> {
        List<List<String>> extractedData = pdfHandler.extractDataUsingRegex(file);
        consolidatedData.addAll(extractedData);
    });

    // Guardar los resultados en un archivo CSV
    FileWriterHelper.saveToCSV(consolidatedData, outputFilePath);
    System.out.println("Archivos procesados y resultados guardados en: "+ outputFilePath);
    }

    private void iterateOverDirectory(String directory, String fileExtension, FileProcessor fileProcessor) throws IOException {
        DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(directory), "*." + fileExtension);
        for (Path filePath : directoryStream) {
            File file = filePath.toFile();
            System.out.println("Procesando archivo: " + file.getName());
            fileProcessor.process(file); // Delegar la lógica al `fileProcessor`.
        }
    }
}
