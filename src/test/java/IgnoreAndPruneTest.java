import com.github.alphahelix00.jsonequals.JsonCompareResult;
import com.github.alphahelix00.jsonequals.JsonEquals;
import com.github.alphahelix00.jsonequals.JsonRoot;
import org.junit.Test;

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

    private static final String GCT_A = "tests/ignore_prune_a.json";
    private static final String GCT_B = "tests/ignore_prune_b.json";

    @Test
    public void test() {
        try {
            JsonEquals.setDebugMode(true);
            String rawA = new String(Files.readAllBytes(Paths.get(GCT_A)));
            String rawB = new String(Files.readAllBytes(Paths.get(GCT_B)));
            JsonRoot treeA = JsonRoot.from(rawA);
            JsonRoot treeB = JsonRoot.from(rawB);

            List<String> ignoreFields = new ArrayList<>();
            Map<String, String> pruneFields = new HashMap<>();

            ignoreFields.add("$[*].data.last_updated");
            pruneFields.put("$[*].data.identities[*]:installed", "false");

            JsonCompareResult result = treeA.compareTo(treeB, ignoreFields, pruneFields);
            result.getInequalityMessages().forEach(System.out::println);

            assertTrue(result.isEqual());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
