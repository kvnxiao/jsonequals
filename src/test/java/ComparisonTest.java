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
    private static String multiArrayTest;
    private static String oooArray1;
    private static String oooArray2;

    @Before
    public void setup() {
        try {
            bookTest = new String(Files.readAllBytes(Paths.get(TEST_FOLDER + "book.json")));
            arrayTest = new String(Files.readAllBytes(Paths.get(TEST_FOLDER + "array.json")));
            multiArrayTest = new String(Files.readAllBytes(Paths.get(TEST_FOLDER + "multiarray.json")));
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
        JsonTree rootTree = JsonTree.from(multiArrayTest);
        List<String> ignoreList = new ArrayList<>();
        ignoreList.add("$[1]");
        assertTrue(rootTree.compareTo(rootTree, ignoreList, null).isEqual());
    }

    @Test
    public void outOfOrderArrayTest() {
        JsonTree rootTreeA = JsonTree.from(oooArray1);
        JsonTree rootTreeB = JsonTree.from(oooArray2);
        for (String message : rootTreeA.compareTo(rootTreeB).getInequalityMessages()) {
            System.out.println(message);
        }
    }
}
