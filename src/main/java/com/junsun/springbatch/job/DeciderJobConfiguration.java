package com.junsun.springbatch.job;

import java.util.Random;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
/**
 *--job.name=deciderJob version=5
 * 
 * Step이 담당하는 역할이 2개 이상이 됩니다.
실제 해당 Step이 처리해야할 로직외에도 분기처리를 시키기 위해 ExitStatus 조작이 필요합니다.
다양한 분기 로직 처리의 어려움
ExitStatus를 커스텀하게 고치기 위해선 Listener를 생성하고 Job Flow에 등록하는 등 번거로움이 존재합니다.
명확하게 Step들간의 Flow 분기만 담당하면서 다양한 분기처리가 가능한 타입이 있으면 편하겠죠?
그래서 Spring Batch에서는 Step들의 Flow속에서 분기만 담당하는 타입이 있습니다.
JobExecutionDecider
 *
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class DeciderJobConfiguration {
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job deciderJob() {
		return jobBuilderFactory.get("deciderJob").start(startStep()).next(decider()) // 홀수 | 짝수 구분
				.from(decider()) // decider의 상태가
				.on("ODD") // ODD라면
				.to(oddStep()) // oddStep로 간다.
				.from(decider()) // decider의 상태가
				.on("EVEN") // ODD라면
				.to(evenStep()) // evenStep로 간다.
				.end() // builder 종료
				.build();
	}

	@Bean
	public Step startStep() {
		return stepBuilderFactory.get("startStep").tasklet((contribution, chunkContext) -> {
			log.info(">>>>> Start!");
			return RepeatStatus.FINISHED;
		}).build();
	}

	@Bean
	public Step evenStep() {
		return stepBuilderFactory.get("evenStep").tasklet((contribution, chunkContext) -> {
			log.info(">>>>> 짝수입니다.");
			return RepeatStatus.FINISHED;
		}).build();
	}

	@Bean
	public Step oddStep() {
		return stepBuilderFactory.get("oddStep").tasklet((contribution, chunkContext) -> {
			log.info(">>>>> 홀수입니다.");
			return RepeatStatus.FINISHED;
		}).build();
	}

	@Bean
	public JobExecutionDecider decider() {
		return new OddDecider();
	}

	public static class OddDecider implements JobExecutionDecider {

//		//@Override
//		public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
//			Random rand = new Random();
//
//			int randomNumber = rand.nextInt(50) + 1;
//			log.info("랜덤숫자: {}", randomNumber);
//
//			if (randomNumber % 2 == 0) {
//				return new FlowExecutionStatus("EVEN");
//			} else {
//				return new FlowExecutionStatus("ODD");
//			}
//		}

		//Step으로 처리하는게 아니기 때문에 ExitStatus가 아닌 FlowExecutionStatus로 상태를 관리
		@Override
		public FlowExecutionStatus decide(org.springframework.batch.core.JobExecution jobExecution,
				org.springframework.batch.core.StepExecution stepExecution) {
			Random rand = new Random();

			int randomNumber = rand.nextInt(50) + 1;
			log.info("랜덤숫자: {}", randomNumber);

			if (randomNumber % 2 == 0) {
				return new FlowExecutionStatus("EVEN");
			} else {
				return new FlowExecutionStatus("ODD");
			}
		}
	}
}