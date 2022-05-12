package onthelive.threeKindofStt.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class CommonUtil {

    public static void saveFile(String filePath, String dest) throws IOException {
        File f = new File(dest);
        FileUtils.copyURLToFile(new URL(filePath) , f);
    }

    /*
    * TODO 반드시! 실행하기 전 경로 확인을 할 것.... 매우 위험함
    *  테스트 이후로는 public 으로 두지 말고 방법 강구할 것.
    * */
    public static void deleteAllFile(String fileStorePath) throws Exception {
        File f = new File(fileStorePath);
        FileUtils.deleteDirectory(f);
    }


}
