import cc.carm.app.easyupdater.utils.FileFinder;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class FileFinderTest {

    @Test
    public void test() {
        String path = "src/test/resources";
        List<File> files = FileFinder.find(path);
        System.out.println("Found:" + files.size());
        files.stream().map(file -> " -> " + file.getAbsolutePath()).forEach(System.out::println);
    }

}
