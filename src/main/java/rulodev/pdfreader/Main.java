
package rulodev.pdfreader;

/**
 *
 * @author Raul_Torres
 */
public class Main {
    public static void main(String[] args) {
        
        //Ejecuta el metodo main PDFTableExtractor2
        System.out.println("Ejecutando PDFTableExtractor...");
        PDFTableExtractor2.main(args);
        
        //Ejecuta el metdo Main CSVKeywordSearch 
        System.out.println("Ejecutando CSVKeywordSearch...");
        CSVKeywordSearch.main(args);
    }
}
