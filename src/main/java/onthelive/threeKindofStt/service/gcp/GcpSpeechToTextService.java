package onthelive.threeKindofStt.service.gcp;

import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import onthelive.threeKindofStt.service.SpeechToTextService;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class GcpSpeechToTextService implements SpeechToTextService {

    @Override
    public String speechToText(String filePath) throws Exception {
        try (SpeechClient speech = SpeechClient.create()) {

            StringBuffer sb = new StringBuffer();

            String fileName = filePath;
            Path path = Paths.get(fileName);
            byte[] data = Files.readAllBytes(path);
            ByteString audioBytes = ByteString.copyFrom(data);

            RecognitionConfig config =
                    RecognitionConfig.newBuilder()
                            .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                            .setLanguageCode("ko-KR")
                            .setSampleRateHertz(16000)
                            .build();

            RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(audioBytes).build();

            RecognizeResponse response = speech.recognize(config, audio);
            List<SpeechRecognitionResult> results = response.getResultsList();

            for (SpeechRecognitionResult result : results) {
                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                sb.append(alternative.getTranscript());
            }

            return sb.toString();
        }
    }
}
