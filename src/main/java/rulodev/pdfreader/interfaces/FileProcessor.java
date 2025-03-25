
package rulodev.pdfreader.interfaces;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author Raul_Torres
 */
@FunctionalInterface
public interface FileProcessor {
     void process(File file) throws IOException;

}
