## 목표
개념 이해, 스프링 배치, 스케줄러 (젠킨스), 연동 샘플, 최신 버전

## 한셋
### ** 1. 출처 **
### ** 2. 이해 ** 
### ** 3. 코드 **
### ** 4. 오류 **

## 한셋
### ** 1. 출처 **
https://jojoldu.tistory.com/325?category=902551

### ** 2. 이해 ** 
#### 2.1. 배치
- 대용량 데이터 - 배치 어플리케이션은 대량의 데이터를 가져오거나, 전달하거나, 계산하는 등의 처리를 할 수 ​​있어야 합니다.
- 자동화 - 배치 어플리케이션은 심각한 문제 해결을 제외하고는 사용자 개입 없이 실행되어야 합니다.
- 견고성 - 배치 어플리케이션은 잘못된 데이터를 충돌/중단 없이 처리할 수 있어야 합니다.
- 신뢰성 - 배치 어플리케이션은 무엇이 잘못되었는지를 추적할 수 있어야 합니다. (로깅, 알림)
- 성능 - 배치 어플리케이션은 지정한 시간 안에 처리를 완료하거나 동시에 실행되는 다른 어플리케이션을 방해하지 않도록 수행되어야합니다.

#### 2.2. 배치 vs 스케줄러
- 배치 - 일괄처리 vs 스케줄러 - 주기적 실행
- 스케줄러 - 크론탭, 스프링 스케줄러, 젠킨스, 쿼츠

### 2.3. Job 구성 
- Job
- Step
- Tasklet or (Reader, Processer, Writer)

### 2.4. 그 외
- job - job instance - job execution (성공/실패)
- job - BATCH_JOB_INSTANCE - BATCH_JOB_EXECUTION
- Spring Batch는 동일한 Job Parameter로 성공한 기록이 있을때만 재수행이 안된다
- Step은 실제 Batch 작업을 수행하는 역할 -  Batch로 실제 처리하고자 하는 기능과 설정을 모두 포함

### 2.5. Job 내부의 Step들간에 순서 혹은 처리 흐름을 제어
- 지정한 Batch job 실행
--job.name=stepNextJob

- Job 전체 Flow
```
on() -> 캐치할 ExitStatus 지정
to() -> 다음으로 이동할 Step 지정
from() -> 일종의 이벤트 리스너 역할, to()에 포함된 step을 호출
end() -> end는 FlowBuilder를 반환하는 end와 FlowBuilder를 종료하는 end 2개가 있음
```

### 2.6. Batch Status vs. Exit Status
- BatchStatus는 Job 또는 Step 의 실행 결과를 Spring에서 기록할 때 사용하는 Enum
- ExitStatus는 Step의 실행 후 상태
- 기본적으로 ExitStatus의 exitCode는 Step의 BatchStatus와 같도록
- ex) .on("FAILED").to(stepB()) -> Step의 ExitStatus
- ex) .on("COMPLETED WITH SKIPS") -> 커스텀 필요 
```
public class SkipCheckingListener extends StepExecutionListenerSupport {

    public ExitStatus afterStep(StepExecution stepExecution) {
        String exitCode = stepExecution.getExitStatus().getExitCode();
        if (!exitCode.equals(ExitStatus.FAILED.getExitCode()) && 
              stepExecution.getSkipCount() > 0) {
            return new ExitStatus("COMPLETED WITH SKIPS");
        }
        else {
            return null;
        }
    }
}
```

### 2.7.  JobParameter와 Scope
- JobParameter : 외부 혹은 내부에서 파라미터를 받아 여러 Batch 컴포넌트에서 사용할 수 있게 지원
- Scope : Job Parameter를 사용하기 위해선 항상 Spring Batch 전용 Scope를 선언
- @StepScope와 @JobScope 2개 / @Value("#{jobParameters[파라미터명]}")
- @JobScope는 Step 선언문에서 사용 가능하고, @StepScope는 Tasklet이나 ItemReader, ItemWriter,ItemProcessor에서 사용

### 2.8. @StepScope & @JobScope
- @StepScope -  Step의 실행시점에 해당 컴포넌트를 Spring Bean으로 생성
- @JobScope 도 마찬가지 
- 위의 어플리케이션 실행 시점이 아닌 scope가 실행되는 시점으로 지연시킴으로 인한 장점
- 1. JobParameter의 Late Binding이 가능 - Controller나 Service와 같은 비지니스 로직 처리 단계에서 Job Parameter를 할당
- 2. 동일한 컴포넌트를 병렬 혹은 동시에 사용할때 유용

### 2.9.  JobParameter vs 시스템 변수(application.properties)
- 시스템 변수를 사용할 경우 Spring Batch의 Job Parameter 관련 기능을 못쓰게 됨. Parameter 관련 메타 테이블이 전혀 관리되지 않습니다.
- Command line이 아닌 다른 방법으로 Job을 실행하기가 어렵습니다.

### 2.10. Chunk란
- 데이터 덩어리로 작업 할 때 각 커밋 사이에 처리되는 row 수
- Chunk 지향 처리란 한 번에 하나씩 데이터를 읽어 Chunk라는 덩어리를 만든 뒤, Chunk 단위로 트랜잭션
- 개별 item을 처리 후 가공된 데이터들을 별도의 공간에 모은 뒤, Chunk 단위만큼 쌓이게 되면 Writer에 전달하고 Writer는 일괄 저장합니다.
- ChunkOrientedTasklet.class

### 2.11. Page Size vs Chunk Size
- Chunk Size는 한번에 처리될 트랜잭션 단위
- Page Size는 한번에 조회할 Item의 양
- PageSize가 10이고, ChunkSize가 50이라면 ItemReader에서 Page 조회가 5번 일어나면 1번 의 트랜잭션이 발생하여 Chunk가 처리
- 2개 값을 일치시키는 것이 보편적으로 좋은 방법

### 2.12. ItemReader
- Spring Batch가 Chunk 지향 처리를 하고 있으며 이를 Job과 Step으로 구성되어 있음을 배웠습니다.
Step은 Tasklet 단위로 처리되고, Tasklet 중에서 ChunkOrientedTasklet을 통해 Chunk를 처리하며 이를 구성하는 3 요소로 ItemReader, ItemWriter, ItemProcessor가 있음
- ItemReader - read()
- ItemStream - open(), close(), update() / 주기적으로 상태를 저장하고 오류가 발생하면 해당 상태에서 복원/  ItemReader의 상태를 저장하고 실패한 곳에서 다시 실행할 수 있게 해주는 역할
- HibernateItemReader, 그 외 file , xml, json
- https://docs.spring.io/spring-batch/docs/4.0.x/reference/html/readersAndWriters.html#flatFiles

https://jojoldu.tistory.com/336?category=902551
### ** 3. 코드 **
- pring Batch 기능 활성화 어노테이션 (@EnableBatchProcessing)
- Spring Batch의 모든 Job은 @Configuration으로 등록

### ** 4. 오류 **
#### 4.1. 환경구성 (3.0.2, 17, 5.0 -> 2.4.4, 11, 2.44)
- 최신 프로젝트 환경을 맞출려고 하였으나 너무 많은 오류로 진행이 안됨. 일단 예제로 개념을 잡고 추후 마이그레이션 하기로 함.

```
build.gradle 다운그레이드 하고 재빌드후 코드 작성 -> 3.0.2, 17을 사용하면 무조건 5.0으로 맞춰짐.
id 'org.springframework.boot' version '2.4.4'
sourceCompatibility = '11'
testImplementation 'org.springframework.batch:spring-batch-test', version: '2.4.4'
implementation 'org.springframework.boot:spring-boot-starter-batch', version: '2.4.4'
```

- schema-mysql.sql 추가후 실행시 에러

```
BATCH_JOB_INSTANCE 테이블 없음
CREATE TABLE BATCH_JOB_EXECUTION_PARAMS  (
	JOB_EXECUTION_ID BIGINT NOT NULL ,
	PARAMETER_NAME VARCHAR(100) NOT NULL ,
	PARAMETER_TYPE VARCHAR(100) NOT NULL ,
	PARAMETER_VALUE VARCHAR(2500) ,
	IDENTIFYING CHAR(1) NOT NULL ,
	constraint JOB_EXEC_PARAMS_FK foreign key (JOB_EXECUTION_ID)
	references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
);

alter table BATCH_JOB_EXECUTION add JOB_CONFIGURATION_LOCATION varchar(100); 

- Field 'CREATE_TIME' doesn't have a default value
ALTER TABLE vup.BATCH_STEP_EXECUTION MODIFY COLUMN CREATE_TIME datetime(6) DEFAULT NULL NULL;
ALTER TABLE vup.BATCH_JOB_EXECUTION_PARAMS ADD KEY_NAME varchar(100) DEFAULT NULL NULL;
ALTER TABLE vup.BATCH_JOB_EXECUTION_PARAMS ADD TYPE_CD varchar(10) DEFAULT NULL NULL;
- 계속 에러나서 다시 찾아봄
CREATE TABLE BATCH_JOB_EXECUTION_PARAMS  (
    JOB_EXECUTION_ID BIGINT NOT NULL ,
    TYPE_CD VARCHAR(6) NOT NULL ,
	KEY_NAME VARCHAR(100) NOT NULL ,
	STRING_VAL VARCHAR(250) ,
	DATE_VAL DATETIME DEFAULT NULL ,
	LONG_VAL BIGINT ,
	DOUBLE_VAL DOUBLE PRECISION ,
	IDENTIFYING CHAR(1) NOT NULL ,
	constraint JOB_EXEC_PARAMS_FK foreign key (JOB_EXECUTION_ID)
	references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
);
-- 드디어 에러 안남


```








## schema-mysql.sql
CREATE TABLE BATCH_JOB_INSTANCE  (
	JOB_INSTANCE_ID BIGINT  NOT NULL PRIMARY KEY ,
	VERSION BIGINT ,
	JOB_NAME VARCHAR(100) NOT NULL,
	JOB_KEY VARCHAR(32) NOT NULL,
	constraint JOB_INST_UN unique (JOB_NAME, JOB_KEY)
) ENGINE=InnoDB;

CREATE TABLE BATCH_JOB_EXECUTION  (
	JOB_EXECUTION_ID BIGINT  NOT NULL PRIMARY KEY ,
	VERSION BIGINT  ,
	JOB_INSTANCE_ID BIGINT NOT NULL,
	CREATE_TIME DATETIME(6) NOT NULL,
	START_TIME DATETIME(6) DEFAULT NULL ,
	END_TIME DATETIME(6) DEFAULT NULL ,
	STATUS VARCHAR(10) ,
	EXIT_CODE VARCHAR(2500) ,
	EXIT_MESSAGE VARCHAR(2500) ,
	LAST_UPDATED DATETIME(6),
	constraint JOB_INST_EXEC_FK foreign key (JOB_INSTANCE_ID)
	references BATCH_JOB_INSTANCE(JOB_INSTANCE_ID)
) ENGINE=InnoDB;

CREATE TABLE BATCH_JOB_EXECUTION_PARAMS  (
	JOB_EXECUTION_ID BIGINT NOT NULL ,
	PARAMETER_NAME VARCHAR(100) NOT NULL ,
	PARAMETER_TYPE VARCHAR(100) NOT NULL ,
	PARAMETER_VALUE VARCHAR(2500) ,
	IDENTIFYING CHAR(1) NOT NULL ,
	constraint JOB_EXEC_PARAMS_FK foreign key (JOB_EXECUTION_ID)
	references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ENGINE=InnoDB;

CREATE TABLE BATCH_STEP_EXECUTION  (
	STEP_EXECUTION_ID BIGINT  NOT NULL PRIMARY KEY ,
	VERSION BIGINT NOT NULL,
	STEP_NAME VARCHAR(100) NOT NULL,
	JOB_EXECUTION_ID BIGINT NOT NULL,
	CREATE_TIME DATETIME(6) NOT NULL,
	START_TIME DATETIME(6) DEFAULT NULL ,
	END_TIME DATETIME(6) DEFAULT NULL ,
	STATUS VARCHAR(10) ,
	COMMIT_COUNT BIGINT ,
	READ_COUNT BIGINT ,
	FILTER_COUNT BIGINT ,
	WRITE_COUNT BIGINT ,
	READ_SKIP_COUNT BIGINT ,
	WRITE_SKIP_COUNT BIGINT ,
	PROCESS_SKIP_COUNT BIGINT ,
	ROLLBACK_COUNT BIGINT ,
	EXIT_CODE VARCHAR(2500) ,
	EXIT_MESSAGE VARCHAR(2500) ,
	LAST_UPDATED DATETIME(6),
	constraint JOB_EXEC_STEP_FK foreign key (JOB_EXECUTION_ID)
	references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ENGINE=InnoDB;

CREATE TABLE BATCH_STEP_EXECUTION_CONTEXT  (
	STEP_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
	SHORT_CONTEXT VARCHAR(2500) NOT NULL,
	SERIALIZED_CONTEXT TEXT ,
	constraint STEP_EXEC_CTX_FK foreign key (STEP_EXECUTION_ID)
	references BATCH_STEP_EXECUTION(STEP_EXECUTION_ID)
) ENGINE=InnoDB;

CREATE TABLE BATCH_JOB_EXECUTION_CONTEXT  (
	JOB_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
	SHORT_CONTEXT VARCHAR(2500) NOT NULL,
	SERIALIZED_CONTEXT TEXT ,
	constraint JOB_EXEC_CTX_FK foreign key (JOB_EXECUTION_ID)
	references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ENGINE=InnoDB;

CREATE TABLE BATCH_STEP_EXECUTION_SEQ (
	ID BIGINT NOT NULL,
	UNIQUE_KEY CHAR(1) NOT NULL,
	constraint UNIQUE_KEY_UN unique (UNIQUE_KEY)
) ENGINE=InnoDB;

INSERT INTO BATCH_STEP_EXECUTION_SEQ (ID, UNIQUE_KEY) select * from (select 0 as ID, '0' as UNIQUE_KEY) as tmp where not exists(select * from BATCH_STEP_EXECUTION_SEQ);

CREATE TABLE BATCH_JOB_EXECUTION_SEQ (
	ID BIGINT NOT NULL,
	UNIQUE_KEY CHAR(1) NOT NULL,
	constraint UNIQUE_KEY_UN unique (UNIQUE_KEY)
) ENGINE=InnoDB;

INSERT INTO BATCH_JOB_EXECUTION_SEQ (ID, UNIQUE_KEY) select * from (select 0 as ID, '0' as UNIQUE_KEY) as tmp where not exists(select * from BATCH_JOB_EXECUTION_SEQ);

CREATE TABLE BATCH_JOB_SEQ (
	ID BIGINT NOT NULL,
	UNIQUE_KEY CHAR(1) NOT NULL,
	constraint UNIQUE_KEY_UN unique (UNIQUE_KEY)
) ENGINE=InnoDB;

INSERT INTO BATCH_JOB_SEQ (ID, UNIQUE_KEY) select * from (select 0 as ID, '0' as UNIQUE_KEY) as tmp where not exists(select * from BATCH_JOB_SEQ);












##출처
https://jojoldu.tistory.com/325?category=902551
- 쉽게 설명이 되어 따라하면서 개념을 이해할려고 하였으나 5.0으로 버전업되면 deprecated된게 많아 개념만 이해
- @EnableBatchProcessing //배치시작
- Job 안에는 여러 Step이 존재하고, Step 안에 Tasklet 혹은 Reader & Processor & Writer 묶음이 존재합니다.

##11
https://mindasom.tistory.com/168
- 대부분의 예제가 jdk1.8 / batch4.0 버전이라 jdk17/batch5.0으로 시작하는것에 맞지 않음.
- 위의 예제를 참고로 5.0버전으로 버전업함
에러발생
Description:

Parameter 1 of method simpleStep1 in com.junsun.springbatch.job.SimpleJobConfiguration required a bean of type 'org.springframework.batch.core.step.tasklet.Tasklet' that could not be found.


Action:

Consider defining a bean of type 'org.springframework.batch.core.step.tasklet.Tasklet' in your configuration.
에러나서 폐기

##ㄴㅇㄹ 11.23 ~ 2022.12.24일에 버전업됨.
https://github.com/spring-projects/spring-batch/wiki/Spring-Batch-5.0-Migration-Guide
https://spring.io/blog/2022/11/24/spring-batch-5-0-goes-ga
위의 마이그레이션 가이드를 보고 다시 작성
기 추가했던 쿼츠가 에러 발생. 제거하고 다시 실행

당연하게 예제가 없다. 그래서 공식 가이드를 참고하여 만들기로 함.


##정리
https://warpgate3.tistory.com/entry/Spring-Batch-Sample-Code-1
- JOB: "매일 아침 6시마다 집 앞 공원에서 트랙을 3바퀴 돌고 온다."
이렇게 Job 을 선언하고 해당 Job을 수행하기 위한 Step 을 정의 한다
Step 1: "아침에 6시에 모닝콜이 울린다. 모닝콜을 끈다."
Step 2: "물을 한 잔 먹고 화장실을 간다. "
Step 3: "간단하게 세수를 한다."
Step 4: "신발끈을 묶는다."



##깨달음

##포함내용
1. 배치 구성
2. 배치 5.0버전 코드 작성
3. 