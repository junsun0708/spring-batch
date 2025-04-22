# spring-batch
스프링 배치 테스트

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