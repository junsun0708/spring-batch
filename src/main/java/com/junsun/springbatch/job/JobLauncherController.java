package com.junsun.springbatch.job;



import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 즉, 개발자가 원하는 어느 타이밍이든 Job Parameter를 생성하고 Job을 수행할 수 있음을 알 수 있습니다.
Job Parameter를 각각의 Batch 컴포넌트들이 사용하면 되니 변경이 심한 경우에도 쉽게 대응할 수 있습니다.
웹서버에서 Batch를 관리하는 것은 권장하지 않습니다
 *
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class JobLauncherController {

    private final JobLauncher jobLauncher;
    private final Job job;

    @GetMapping("/launchjob")
    public String handle(@RequestParam("fileName") String fileName) throws Exception {

        try {
            JobParameters jobParameters = new JobParametersBuilder()
                                    .addString("input.file.name", fileName)
                                    .addLong("time", System.currentTimeMillis())
                                    .toJobParameters();
            jobLauncher.run(job, jobParameters);
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        return "Done";
    }
}

/**
 * @Bean과 @StepScope를 함께 쓰는 것은 @Scope (value = "step", proxyMode = TARGET_CLASS)로 표시하는 것과 같습니다.
 * https://jojoldu.tistory.com/132
 */
