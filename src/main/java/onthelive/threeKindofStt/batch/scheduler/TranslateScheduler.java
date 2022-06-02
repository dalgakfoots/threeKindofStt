package onthelive.threeKindofStt.batch.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TranslateScheduler {

    private final Job threeKindOfSttJob;
    private final JobLauncher jobLauncher;

    @Scheduled(fixedDelay = 600 * 1000L)
    public void executeJob() {
        try {
            jobLauncher.run(
                    threeKindOfSttJob,
                    new JobParametersBuilder().addString("datetime", LocalDateTime.now().toString())
                            .toJobParameters()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
