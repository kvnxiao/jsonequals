import com.github.alphahelix00.jsonequals.JsonTree;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by zxiao on 2/15/17.
 */
public class ComparisonTest {

    private static final String TEST_FOLDER = "tests/";
    private static String bookTest;
    private static String arrayTest;
    private static String multiArrayTest;

    @Before
    public void setup() {
        try {
            bookTest = new String(Files.readAllBytes(Paths.get(TEST_FOLDER + "book.json")));
            arrayTest = new String(Files.readAllBytes(Paths.get(TEST_FOLDER + "array.json")));
            multiArrayTest = new String(Files.readAllBytes(Paths.get(TEST_FOLDER + "multiarray.json")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void bookTest() {
        JsonTree rootTree = JsonTree.from(bookTest);
        rootTree.compareTo(rootTree);
    }

    @Test
    public void simpleArrayTest() {
        JsonTree rootTree = JsonTree.from(arrayTest);
        rootTree.compareTo(rootTree);
    }

    @Test
    public void multiArrayTest() {
        JsonTree rootTree = JsonTree.from(multiArrayTest);
        rootTree.compareTo(rootTree);
    }
}
