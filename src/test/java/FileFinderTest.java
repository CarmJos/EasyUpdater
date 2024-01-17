import cc.carm.app.easyupdater.utils.FileFinder;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class FileFinderTest {


    @Test
    public void test() {
        List<File> files = FileFinder.findFiles("C:\\Users\\(.*)\\Desktop\\(.*).lnk");

        System.out.println(files.size());
        for (File file : files) {
            System.out.println(" -> " + file.getAbsolutePath());
        }

    }

}
