package onthelive.threeKindofStt.entity;

import com.google.gson.Gson;
import lombok.Data;
import onthelive.threeKindofStt.service.SpeechToTextService;

import java.util.HashMap;

@Data
public class SpeechToTextJob {

    private Long projectId;
    private Long documentId;
    private Long sectionId;
    private Long segmentId;

    private String value;
    private SttValue sttValue;
    private SpeechToTextService speechToTextService;
    private String sttResult;

    private String processCode;
    private String state;

    private Long jobMasterId;
    private Long jobSubId;
    private Long userId;

    private Long historyCnt;


    public void valueToSttValue(String value) {
        SttValue resultValue = new Gson().fromJson(value, SttValue.class);
        setSttValue(resultValue);
    }

    public void runStt(String filePath) throws Exception {
        String result = speechToTextService.speechToText(filePath);
        HashMap<String, String> temp = new HashMap<>();
        temp.put("sttText", result);
        String sttResult = new Gson().toJson(temp);
        setSttResult(sttResult);
    }
}
