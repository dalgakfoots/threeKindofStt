package onthelive.threeKindofStt.batch.step;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AfterSttUpdateSectionTaskletBean {

    private final StepBuilderFactory stepBuilderFactory;
    private final AfterSttUpdateSectionTasklet afterSttUpdateSectionTasklet;

    @Bean
    public Step afterSttUpdateSectionStep() throws Exception {
        return stepBuilderFactory.get("afterSttUpdateSectionStep")
                .tasklet(afterSttUpdateSectionTasklet)
                .build();
    }

}
