package onthelive.threeKindofStt;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableBatchProcessing
@EnableScheduling
public class ThreeKindofSttApplication {

	public static void main(String[] args) {
		SpringApplication.run(ThreeKindofSttApplication.class, args);
	}

}
