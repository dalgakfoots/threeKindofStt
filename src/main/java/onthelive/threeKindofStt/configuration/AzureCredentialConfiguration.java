package onthelive.threeKindofStt.configuration;

import com.microsoft.cognitiveservices.speech.SpeechConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureCredentialConfiguration {

    @Value("${azure.subscriptionKey}")
    private String subscriptionKey;
    @Value("${azure.serviceRegion}")
    private String serviceRegion;

    @Bean(name = "speechConfig")
    public SpeechConfig getSpeechConfig() {
        SpeechConfig speechConfig = SpeechConfig.fromSubscription(subscriptionKey, serviceRegion);
        speechConfig.setSpeechRecognitionLanguage("ko-KR");
        return speechConfig;
    }
}
