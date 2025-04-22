package com.junsun.springbatch.job;

<<<<<<< HEAD
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//@Slf4j 
//@RequiredArgsConstructor
//@Configuration
//@EnableBatchProcessing
public class SimpleJobConfiguration {
/* 4->5 버전업으로 deprecated됨 대부분의 예제가 안돌아가서 새로 만듬. 아래에 추가
	private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
=======
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j // log 사용을 위한 lombok 어노테이션
@RequiredArgsConstructor // 생성자 DI를 위한 lombok 어노테이션
@Configuration
public class SimpleJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory; // 생성자 DI 받음
    private final StepBuilderFactory stepBuilderFactory; // 생성자 DI 받음

    //job - job instance - job execution
>>>>>>> 0de305b00dfd8ed9431b923100be6afc92d85f26
    
    @Bean
    public Job simpleJob() {
        return jobBuilderFactory.get("simpleJob")
<<<<<<< HEAD
                .start(simpleStep1())
                .build();
    }
    
    @Bean
    public Step simpleStep1() {
        return stepBuilderFactory.get("simpleStep1")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is Step1");
=======
                .start(simpleStep1(null))
                .next(simpleStep2(null))
                .build();
    }

    @Bean
    @JobScope
    //Job Parameter로 받은 값을 로그에 추가로 출력시키는 기능
    //Program arguments에 requestDate=20180805 추가후 BATCH_JOB_INSTANCE 확인
    //BATCH_JOB_INSTANCE - BATCH_JOB_EXECUTION 부모-자식 (잡 - 잡의 성공/실패내역)
    public Step simpleStep1(@Value("#{jobParameters[requestDate]}") String requestDate) {
        return stepBuilderFactory.get("simpleStep1")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is Step1");
                    log.info(">>>>> requestDate = {}", requestDate);
                    return RepeatStatus.FINISHED;
                    //throw new IllegalArgumentException("step1 실패" + requestDate);
                })
                .build();
    }
    
    //동일한 Job Parameter로 2번 실행했는데 같은 파라미터로 실행되었다는 에러가 발생하지 않았다
    //Spring Batch는 동일한 Job Parameter로 성공한 기록이 있을때만 재수행이 안된다
    public Step simpleStep2(@Value("#{jobParameters[requestDate]}") String requestDate) {
        return stepBuilderFactory.get("simpleStep2")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is Step2");
                    log.info(">>>>> requestDate = {}", requestDate);
>>>>>>> 0de305b00dfd8ed9431b923100be6afc92d85f26
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
<<<<<<< HEAD
    */
//@Autowired
//private JobRepository jobRepository;
//
//@Bean
//public Tasklet myTasklet() {
//   return new MyTasklet();
//}
//
//@Bean
//public Job simpleJob(Step step) {
//	return new JobBuilder("simpleJob", this.jobRepository).start(step).build();
//}
//
//@Bean
//public Step simpleStep1(JobRepository jobRepository, Tasklet myTasklet, PlatformTransactionManager transactionManager) {
//	return new StepBuilder("simpleStep1", jobRepository).tasklet(myTasklet, transactionManager).build();
//}
	
//    @Bean
//    public Tasklet myTasklet() {
//       return new MyTasklet();
//    }
//
//    @Bean
//    public Step myStep(JobRepository jobRepository, Tasklet myTasklet, PlatformTransactionManager transactionManager) {
//        return new StepBuilder("myStep", jobRepository)
//                .tasklet(myTasklet, transactionManager) // or .chunk(chunkSize, transactionManager)
//                .build();
//    }
=======
    
   
>>>>>>> 0de305b00dfd8ed9431b923100be6afc92d85f26
}
