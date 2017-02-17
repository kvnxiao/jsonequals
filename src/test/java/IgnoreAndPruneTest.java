import com.github.alphahelix00.jsonequals.JsonCompareResult;
import com.github.alphahelix00.jsonequals.JsonEquals;
import com.github.alphahelix00.jsonequals.JsonRoot;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * Created by zxiao on 2/16/17.
 */
public class IgnoreAndPruneTest {

    private static final String ignoreAndPruneA = "tests/ignore_prune_a.json";
    private static final String ignoreAndPruneB = "tests/ignore_prune_b.json";
    private static final String pruneA = "tests/prune_a.json";
    private static final String pruneb = "tests/prune_b.json";

    @Before
    public void enableDebug() {
        JsonEquals.setDebugMode(true);
    }

    @Test
    public void pruneTest() throws IOException {
        String rawA = new String(Files.readAllBytes(Paths.get(pruneA)));
        String rawB = new String(Files.readAllBytes(Paths.get(pruneb)));
        JsonRoot jsonA = JsonRoot.from(rawA);
        JsonRoot jsonB = JsonRoot.from(rawB);

        Map<String, String> pruneFields = new HashMap<>();
        pruneFields.put("$.array[*]:id.isValid", "false");

        JsonCompareResult result = jsonA.compareToWithPrune(jsonB, pruneFields);
        result.getInequalityMessages().forEach(System.out::println);

        assertTrue(result.isEqual());
    }

    @Test
    public void ignoreAndPruneTest() throws IOException {
        String rawA = new String(Files.readAllBytes(Paths.get(ignoreAndPruneA)));
        String rawB = new String(Files.readAllBytes(Paths.get(ignoreAndPruneB)));
        JsonRoot jsonA = JsonRoot.from(rawA);
        JsonRoot jsonB = JsonRoot.from(rawB);

        List<String> ignoreFields = new ArrayList<>();
        Map<String, String> pruneFields = new HashMap<>();

        ignoreFields.add("$[*].data.last_updated");
        pruneFields.put("$[*].data.identities[*]:installed", "false");

        JsonCompareResult result = jsonA.compareTo(jsonB, ignoreFields, pruneFields);
        result.getInequalityMessages().forEach(System.out::println);

        assertTrue(result.isEqual());
    }


}
