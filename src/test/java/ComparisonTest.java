import com.github.alphahelix00.jsonequals.JsonCompareResult;
import com.github.alphahelix00.jsonequals.JsonEquals;
import com.github.alphahelix00.jsonequals.JsonRoot;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by zxiao on 2/15/17.
 */
public class ComparisonTest {

    private static final String TEST_FOLDER = "tests/";

    @Before
    public void enableDebug() {
        JsonEquals.setDebugMode(true);
    }

    @Test
    public void bookTest() throws IOException {
        String bookTestA = new String(Files.readAllBytes(Paths.get(TEST_FOLDER + "book_a.json")));
        String bookTestB = new String(Files.readAllBytes(Paths.get(TEST_FOLDER + "book_b.json")));

        JsonRoot jsonA = JsonRoot.from(bookTestA);
        JsonRoot jsonB = JsonRoot.from(bookTestB);
        JsonCompareResult result = jsonA.compareTo(jsonB);

        assertTrue(result.isEqual());
    }

    @Test
    public void simpleNotEqualsArrayTest() throws IOException {
        String arrayTestA = new String(Files.readAllBytes(Paths.get(TEST_FOLDER + "array_a.json")));
        String arrayTestB = new String(Files.readAllBytes(Paths.get(TEST_FOLDER + "array_b.json")));

        JsonRoot jsonA = JsonRoot.from(arrayTestA);
        JsonRoot jsonB = JsonRoot.from(arrayTestB);
        JsonCompareResult result = jsonA.compareTo(jsonB);

        assertEquals(1, result.getInequalityCount());
        assertFalse(result.isEqual());
    }

    @Test
    public void multiArrayTest() throws IOException {
        String multiArrayTestA = new String(Files.readAllBytes(Paths.get(TEST_FOLDER + "multiarray_a.json")));
        String multiArrayTestB = new String(Files.readAllBytes(Paths.get(TEST_FOLDER + "multiarray_b.json")));

        JsonRoot jsonA = JsonRoot.from(multiArrayTestA);
        JsonRoot jsonB = JsonRoot.from(multiArrayTestB);
        JsonCompareResult result = jsonA.compareTo(jsonB);

        assertTrue(result.isEqual());
    }

    @Test
    public void testMultipleArrayObjects() throws IOException {
        String rawA = new String(Files.readAllBytes(Paths.get(TEST_FOLDER + "multi_array_objects_a.json")));
        String rawB = new String(Files.readAllBytes(Paths.get(TEST_FOLDER + "multi_array_objects_b.json")));

        JsonRoot jsonA = JsonRoot.from(rawA);
        JsonRoot jsonB = JsonRoot.from(rawB);
        JsonCompareResult result = jsonA.compareTo(jsonB);

        assertTrue(result.isEqual());
    }

}
