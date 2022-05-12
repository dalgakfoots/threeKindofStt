package onthelive.threeKindofStt.batch.step;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onthelive.threeKindofStt.entity.FileInfo;
import onthelive.threeKindofStt.entity.SpeechToTextJob;
import onthelive.threeKindofStt.entity.enums.SttType;
import onthelive.threeKindofStt.service.azure.AzureSpeechToTextService;
import onthelive.threeKindofStt.service.gcp.GcpSpeechToTextService;
import onthelive.threeKindofStt.service.naver.NaverSpeechToTextService;
import onthelive.threeKindofStt.util.CommonUtil;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class SpeechToTextProcessor implements ItemProcessor<SpeechToTextJob , SpeechToTextJob> {

    private final GcpSpeechToTextService gcpSpeechToTextService;
    private final AzureSpeechToTextService azureSpeechToTextService;
    private final NaverSpeechToTextService naverSpeechToTextService;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public SpeechToTextJob process(SpeechToTextJob item) throws Exception {

        Long historyId = updateTableProcess(item);

        item.setHistoryCnt(historyId);
        item.valueToSttValue(item.getValue());
        item.setProcessCode("STT");

        String destFile = saveAndReturnFilePath(
                item.getSttValue()
                .getAudioFile()
        );

        SttType sttType = item.getSttValue().getStt_source();

        if(sttType == SttType.GOOGLE) {
            item.setSpeechToTextService(gcpSpeechToTextService);
        } else if (sttType == SttType.AZURE) {
            item.setSpeechToTextService(azureSpeechToTextService);
        } else if (sttType == SttType.NAVER) {
            item.setSpeechToTextService(naverSpeechToTextService);
        }

        try {
            item.runStt(destFile);
            item.setState("COMPLETE");
            return item;
        } catch (Exception e) {
            e.printStackTrace();
            item.setState("FAIL");
            failProcess(item);
            return item;
        }
    }

    /* PRIVATE METHODS */

    private Long updateTableProcess(SpeechToTextJob item) {
        Long jobMasterId = item.getJobMasterId();
        Long jobSubId = item.getJobSubId();
        Long userId = item.getUserId();

        Long historyId = getHistoryId(jobMasterId, jobSubId);

        jdbcTemplate.update("UPDATE job_masters SET current_state = 'PROGRESS', updated_datetime = now() WHERE id = ?", jobMasterId);
        jdbcTemplate.update("UPDATE job_subs SET state = 'PROGRESS', updated_datetime = now() WHERE job_master_id = ? and id = ? ", jobMasterId, jobSubId);

        jdbcTemplate.update("INSERT INTO job_sub_histories (id, job_master_id, job_sub_id, user_id, process_code, state, reject_state) " +
                        "VALUES (?, ? , ? , ? , 'STT', 'PROGRESS' , '0')",
                historyId, jobMasterId, jobSubId, userId
        );

        return historyId;
    }

    private Long getHistoryId(Long jobMasterId, Long jobSubId) {
        Long historyId = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM job_sub_histories WHERE job_master_id = ? AND job_sub_id = ?", Long.class,
                jobMasterId, jobSubId
        );
        return historyId;
    }

    private void failProcess(SpeechToTextJob item) {
        log.info("failProcess.....");

        jdbcTemplate.update("UPDATE job_masters SET current_state = 'FAIL', updated_datetime = now() WHERE id = ?", item.getJobMasterId());
        jdbcTemplate.update("UPDATE job_subs SET state = 'FAIL', updated_datetime = now() WHERE job_master_id = ? and id = ? ", item.getJobMasterId(), item.getJobSubId());

        jdbcTemplate.update("INSERT INTO job_sub_histories (id, job_master_id, job_sub_id, user_id, process_code, state, reject_state) " +
                        "VALUES (?, ? , ? , ? , 'machine_translation', 'FAIL' , '0') on duplicate key update state = 'FAIL'",
                item.getHistoryCnt() + 1, item.getJobMasterId(), item.getJobSubId(), item.getUserId()
        );
    }

    private String saveAndReturnFilePath(FileInfo fileInfo) throws IOException {
        String filePath = fileInfo.getFilePath();
        String fileName = fileInfo.getStorageFileName();
        String destFile = "/Users/dalgakfoot/Documents/HUFS/fileStorage/" + fileName;

        CommonUtil.saveFile(filePath , destFile);
        return destFile;
    }

}
