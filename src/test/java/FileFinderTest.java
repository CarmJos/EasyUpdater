import cc.carm.app.easyupdater.utils.FileFinder;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class FileFinderTest {

    @Test
    public void test() {
        List<File> files = FileFinder.find("src\\main\\java\\cc\\carm\\app\\easyupdater\\utils\\(.*).java");
        System.out.println("Found:" + files.size());
        files.stream().map(file -> " -> " + file.getAbsolutePath()).forEach(System.out::println);
    }

}
