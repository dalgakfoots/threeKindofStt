package onthelive.threeKindofStt.configuration;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class GcpCredentialConfiguration {

    /*
     * GCP 사용을 위한 Credential 환경변수 처리 Configuration
     * 시스템의 환경변수를 코드로 조작하고 있어 보안상 문제가 발생할 수 있음.
     * https://stackoverflow.com/questions/51127494/define-google-application-credentials-for-google-cloud-speech-java-desktop-app
     * TODO 실제 서비스 이상의 결과물에서는 Credential 환경변수 처리 방법을 반드시 강구해야함.
     */

    public void setEnv(Map<String, String> newenv)
            throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException {
        try {
            Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
            Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
            theEnvironmentField.setAccessible(true);
            Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
            env.putAll(newenv);
            Field theCaseInsensitiveEnvironmentField = processEnvironmentClass
                    .getDeclaredField("theCaseInsensitiveEnvironment");
            theCaseInsensitiveEnvironmentField.setAccessible(true);
            Map<String, String> cienv = (Map<String, String>) theCaseInsensitiveEnvironmentField.get(null);
            cienv.putAll(newenv);
        } catch (NoSuchFieldException e) {
            Class[] classes = Collections.class.getDeclaredClasses();
            Map<String, String> env = System.getenv();
            for (Class cl : classes) {
                if ("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
                    Field field = cl.getDeclaredField("m");
                    field.setAccessible(true);
                    Object obj = field.get(env);
                    Map<String, String> map = (Map<String, String>) obj;
                    map.clear();
                    map.putAll(newenv);
                }
            }
        }
    }

    @PostConstruct
    public void runSetEnv() throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException {
        // GCP 사용을 위한 환경변수 적용 시작
        Map<String, String> google = new HashMap<>();
        google.put("GOOGLE_APPLICATION_CREDENTIALS",
                "/Users/dalgakfoot/Downloads/threeKindofStt/skillful-eon-349406-6ce935293e40.json");
//        google.put("GOOGLE_APPLICATION_CREDENTIALS",
//                "/opt/gcpStt/credentials/credentials.json");

        setEnv(google);
        // GCP 사용을 위한 환경변수 적용 끝
    }
}
