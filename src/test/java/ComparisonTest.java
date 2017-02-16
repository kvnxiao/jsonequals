import com.github.alphahelix00.jsonequals.JsonCompareResult;
import com.github.alphahelix00.jsonequals.JsonEquals;
import com.github.alphahelix00.jsonequals.JsonTree;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by zxiao on 2/15/17.
 */
public class ComparisonTest {

    private static final String TEST_FOLDER = "tests/";
    private static String bookTest;
    private static String arrayTest;
    private static String multiArrayTest1;
    private static String multiArrayTest2;
    private static String oooArray1;
    private static String oooArray2;

    @Before
    public void setup() {
        try {
            bookTest = new String(Files.readAllBytes(Paths.get(TEST_FOLDER + "book.json")));
            arrayTest = new String(Files.readAllBytes(Paths.get(TEST_FOLDER + "array.json")));
            multiArrayTest1 = new String(Files.readAllBytes(Paths.get(TEST_FOLDER + "multiarray_a.json")));
            multiArrayTest2 = new String(Files.readAllBytes(Paths.get(TEST_FOLDER + "multiarray_b.json")));
            oooArray1 = new String(Files.readAllBytes(Paths.get(TEST_FOLDER + "outoforder1.json")));
            oooArray2 = new String(Files.readAllBytes(Paths.get(TEST_FOLDER + "outoforder2.json")));
            JsonEquals.setDebugMode(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void bookTest() {
        JsonTree rootTree = JsonTree.from(bookTest);
        assertTrue(rootTree.compareTo(rootTree).isEqual());
    }

    @Test
    public void simpleArrayTest() {
        JsonTree rootTree = JsonTree.from(arrayTest);
        assertTrue(rootTree.compareTo(rootTree).isEqual());
    }

    @Test
    public void multiArrayTest() {
        JsonTree rootTreeA = JsonTree.from(multiArrayTest1);
        JsonTree rootTreeB = JsonTree.from(multiArrayTest2);
        JsonCompareResult result = rootTreeA.compareTo(rootTreeB);
        result.getSuccessMessages().forEach(System.out::println);
        result.getInequalityMessages().forEach(System.out::println);
        assertTrue(result.isEqual());
    }

    @Test
    public void outOfOrderArrayTest() {
        JsonTree rootTreeA = JsonTree.from(oooArray1);
        JsonTree rootTreeB = JsonTree.from(oooArray2);
        JsonCompareResult result = rootTreeA.compareTo(rootTreeB);
        assertTrue(result.isEqual());
    }
}
