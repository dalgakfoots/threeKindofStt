package onthelive.threeKindofStt.service.azure;

import com.microsoft.cognitiveservices.speech.*;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onthelive.threeKindofStt.service.SpeechToTextService;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
@Slf4j
public class AzureSpeechToTextService implements SpeechToTextService {

    private final SpeechConfig speechConfig;

    @Override
    public String speechToText(String filePath) throws Exception {
        AudioConfig audioConfig = AudioConfig.fromWavFileInput(filePath);
        SpeechRecognizer speechRecognizer = new SpeechRecognizer(speechConfig, audioConfig);

        Future<SpeechRecognitionResult> task = speechRecognizer.recognizeOnceAsync();
        SpeechRecognitionResult result = task.get();

        if(result.getReason() == ResultReason.RecognizedSpeech) {
            return result.getText();
        } else if (result.getReason() == ResultReason.NoMatch) {
            throw new Exception("Speech could not be recognized.");
        } else if (result.getReason() == ResultReason.Canceled) {
            CancellationDetails cancelDetail = CancellationDetails.fromResult(result);

            if (cancelDetail.getReason() == CancellationReason.Error) {
                throw new Exception("ErrorDetails : "+cancelDetail.getErrorDetails());
            } else {
                throw new Exception("Canceled: Reason : " + cancelDetail.getReason());
            }
        }

        return null;
    }
}
