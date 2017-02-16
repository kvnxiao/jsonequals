import com.github.alphahelix00.jsonequals.JsonCompareResult;
import com.github.alphahelix00.jsonequals.JsonEquals;
import com.github.alphahelix00.jsonequals.JsonRoot;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by zxiao on 2/15/17.
 */
public class ComparisonTest {

    private static final String TEST_FOLDER = "tests/";
    private static String bookTestA;
    private static String bookTestB;
    private static String arrayTestA;
    private static String arrayTestB;
    private static String multiArrayTestA;
    private static String multiArrayTestB;

    @Before
    public void setup() {
        try {
            bookTestA = new String(Files.readAllBytes(Paths.get(TEST_FOLDER + "book_a.json")));
            bookTestB = new String(Files.readAllBytes(Paths.get(TEST_FOLDER + "book_b.json")));
            arrayTestA = new String(Files.readAllBytes(Paths.get(TEST_FOLDER + "array_a.json")));
            arrayTestB = new String(Files.readAllBytes(Paths.get(TEST_FOLDER + "array_b.json")));
            multiArrayTestA = new String(Files.readAllBytes(Paths.get(TEST_FOLDER + "multiarray_a.json")));
            multiArrayTestB = new String(Files.readAllBytes(Paths.get(TEST_FOLDER + "multiarray_b.json")));
            JsonEquals.setDebugMode(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void bookTest() {
        JsonRoot jsonA = JsonRoot.from(bookTestA);
        JsonRoot jsonB = JsonRoot.from(bookTestB);
        JsonCompareResult result = jsonA.compareTo(jsonB);
        assertTrue(result.isEqual());
    }

    @Test
    public void simpleNotEqualsArrayTest() {
        JsonRoot jsonA = JsonRoot.from(arrayTestA);
        JsonRoot jsonB = JsonRoot.from(arrayTestB);
        JsonCompareResult result = jsonA.compareTo(jsonB);
        result.getInequalityMessages().forEach(System.out::println);
        assertFalse(result.isEqual());
    }

    @Test
    public void multiArrayTest() {
        JsonRoot jsonA = JsonRoot.from(multiArrayTestA);
        JsonRoot jsonB = JsonRoot.from(multiArrayTestB);
        JsonCompareResult result = jsonA.compareTo(jsonB);
        result.getSuccessMessages().forEach(System.out::println);
        result.getInequalityMessages().forEach(System.out::println);
        assertTrue(result.isEqual());
    }

}
