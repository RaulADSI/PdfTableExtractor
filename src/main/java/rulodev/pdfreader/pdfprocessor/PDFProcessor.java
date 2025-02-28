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
    private String outputDir;

    public PDFProcessor(String inputDir, String outPutDir) {
        this.inputDir = inputDir;
        this.outputDir = outPutDir;
    }

    public void processFiles() {
        try {
            Files.createDirectories(Paths.get(outputDir));

            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(inputDir), "*.pdf");
            for (Path pdfFilePath : directoryStream) {
                processPDFFile(pdfFilePath.toFile());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void processPDFFile(File pdfFile){
         try {
            PDDocument document = PDDocument.load(pdfFile);
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            String[] lines = text.split("\\r?\\n");

            List<List<String>> table = new ArrayList<>();
            for (String line : lines) {
                String[] columns = line.split("\\s+");
                List<String> row = new ArrayList<>();
                for (String column : columns) {
                    row.add(column);
                }
                table.add(row);
            }

            String outputFilePath = outputDir + pdfFile.getName().replace(".pdf", ".csv");
            saveToCSV(table, outputFilePath);

            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
     private void saveToCSV(List<List<String>> table, String filePath) {
        try {
            File outputFile = new File(filePath);
            outputFile.getParentFile().mkdirs();

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
