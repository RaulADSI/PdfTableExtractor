
package rulodev.pdfreader.pdfprocessor;

/**
 *
 * @author Raul_Torres
 */
public class TextProcessor {
     
    public static String removeSpecialCharacters(String input) {
        if (input == null) {
            return ""; // Devuelve una cadena vacía si la entrada es null.
        }
        return input.replaceAll("[^a-zA-Z0-9\\s.]", ""); // Elimina todo excepto letras, números y espacios.
    }

}
