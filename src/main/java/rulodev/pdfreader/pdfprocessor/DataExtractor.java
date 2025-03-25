package rulodev.pdfreader.pdfprocessor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 *
 * @author Raul_Torres
 */
public class DataExtractor {

    public List<List<String>> extractDataUsingRegex(File pdfFile) {
        List<List<String>> extractedData = new ArrayList<>();
        try {
            String text;
            try (PDDocument document = PDDocument.load(pdfFile)) {
                PDFTextStripper pdfStripper = new PDFTextStripper();
                text = pdfStripper.getText(document);
            }

            // --- Definir patrones de expresiones regulares ---
            // Patrón para montos:
            // Ejemplo: 1,234.56 o 1234.56 o simplemente 1234
            Pattern amountPattern = Pattern.compile("\\b\\d{1,3}(,\\d{3})*(\\.\\d{2})?\\b");
            // Patrón para fechas:
            Pattern datePattern = Pattern.compile("\\b(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec|January|February|March|April|May|June|July|August|September|October|November|December)\\s\\d{1,2}(,\\s\\d{4}|\\s\\d{4})\\b");
            // Patrón para números de cuenta (suponiendo entre 8 y 20 dígitos consecutivos):
            Pattern accountPattern = Pattern.compile("\\b\\d{8,20}(-?\\d{8,20})*\\b");

            // --- Buscar coincidencias ---
            List<String> amounts = new ArrayList<>();
            Matcher amountMatcher = amountPattern.matcher(text);
            while (amountMatcher.find()) {
                amounts.add(amountMatcher.group());
            }

            List<String> dates = new ArrayList<>();
            Matcher dateMatcher = datePattern.matcher(text);
            while (dateMatcher.find()) {
                dates.add(dateMatcher.group());
            }

            List<String> accountNumbers = new ArrayList<>();
            Matcher accountMatcher = accountPattern.matcher(text);
            while (accountMatcher.find()) {
                accountNumbers.add(accountMatcher.group());
            }

            // Agregar los resultados a la lista general
            extractedData.add(amounts);
            extractedData.add(dates);
            extractedData.add(accountNumbers);

        } catch (IOException e) {
        }
        return extractedData;
    }
}
