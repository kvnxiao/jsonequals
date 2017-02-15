import com.github.alphahelix00.jsonequals.JsonTree;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by zxiao on 2/15/17.
 */
public class ComparisonTest {

    private static final String TEST_FOLDER = "tests/";

    @Test
    public void bookTest() {
        try {
            String source = new String(Files.readAllBytes(Paths.get(TEST_FOLDER + "book.json")));
            JsonTree rootTree = JsonTree.from(source);
            rootTree.compareTo(rootTree);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void simpleArrayTest() {
        try {
            String source = new String(Files.readAllBytes(Paths.get(TEST_FOLDER + "array.json")));
            JsonTree rootTree = JsonTree.from(source);
            rootTree.compareTo(rootTree);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
