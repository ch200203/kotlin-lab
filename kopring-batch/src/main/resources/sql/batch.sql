-- BATCH_JOB_INSTANCE Table: 배치 작업 인스턴스에 대한 정보를 저장하는 테이블
CREATE TABLE BATCH_JOB_INSTANCE
(
    JOB_INSTANCE_ID BIGINT PRIMARY KEY,    -- 유니크한 잡 인스턴스 ID
    VERSION         BIGINT,                -- 버전 정보
    JOB_NAME        VARCHAR(100) NOT NULL, -- 배치 잡 이름 (NULL 불가)
    JOB_KEY         VARCHAR(32)  NOT NULL  -- JobParameter를 직렬화한 값으로 구분되는 키 (NULL 불가)
);

-- BATCH_JOB_EXECUTION_PARAMS Table: JobParameter에 대한 정보를 저장하는 테이블
CREATE TABLE BATCH_JOB_EXECUTION_PARAMS
(
    JOB_EXECUTION_ID BIGINT       NOT NULL,               -- BATCH_JOB_EXECUTION 테이블의 외래키
    PARAMETER_NAME   VARCHAR(100) NOT NULL,               -- 파라미터 이름
    PARAMETER_TYPE   VARCHAR(100) NOT NULL,               -- 파라미터 타입
    PARAMETER_VALUE  VARCHAR(2500),                       -- 파라미터 값
    IDENTIFYING      CHAR(1)      NOT NULL,               -- 파라미터가 유니크한지 여부 (true/false)
    constraint JOB_EXEC_PARAMS_FK foreign key (JOB_EXECUTION_ID)
        references BATCH_JOB_EXECUTION (JOB_EXECUTION_ID) -- 외래키 제약조건
);

-- BATCH_JOB_EXECUTION Table: JobExecution 관련 정보를 저장하는 테이블
CREATE TABLE BATCH_JOB_EXECUTION
(
    JOB_EXECUTION_ID BIGINT PRIMARY KEY,                -- 유니크한 잡 실행 ID
    VERSION          BIGINT,                            -- 버전 정보
    JOB_INSTANCE_ID  BIGINT    NOT NULL,                -- BATCH_JOB_INSTANCE 테이블의 외래키
    CREATE_TIME      TIMESTAMP NOT NULL,                -- 실행이 생성된 시간
    START_TIME       TIMESTAMP DEFAULT NULL,            -- 실행이 시작된 시간
    END_TIME         TIMESTAMP DEFAULT NULL,            -- 실행이 종료된 시간
    STATUS           VARCHAR(10),                       -- 실행 상태 (예: COMPLETED, STARTED)
    EXIT_CODE        VARCHAR(20),                       -- 실행 종료 코드
    EXIT_MESSAGE     VARCHAR(2500),                     -- 실행 종료 메시지 (에러 스택 포함 가능)
    LAST_UPDATED     TIMESTAMP,                         -- 마지막 업데이트 시간
    constraint JOB_INSTANCE_EXECUTION_FK foreign key (JOB_INSTANCE_ID)
        references BATCH_JOB_INSTANCE (JOB_INSTANCE_ID) -- 외래키 제약조건
);

-- BATCH_STEP_EXECUTION Table: StepExecution 관련 정보를 저장하는 테이블
CREATE TABLE BATCH_STEP_EXECUTION
(
    STEP_EXECUTION_ID  BIGINT       NOT NULL PRIMARY KEY, -- 유니크한 스텝 실행 ID
    VERSION            BIGINT       NOT NULL,             -- 버전 정보
    STEP_NAME          VARCHAR(100) NOT NULL,             -- 스텝 이름
    JOB_EXECUTION_ID   BIGINT       NOT NULL,             -- BATCH_JOB_EXECUTION 테이블의 외래키
    CREATE_TIME        TIMESTAMP    NOT NULL,             -- 스텝 실행이 생성된 시간
    START_TIME         TIMESTAMP DEFAULT NULL,            -- 스텝 실행이 시작된 시간
    END_TIME           TIMESTAMP DEFAULT NULL,            -- 스텝 실행이 종료된 시간
    STATUS             VARCHAR(10),                       -- 스텝 실행 상태
    COMMIT_COUNT       BIGINT,                            -- 커밋된 트랜잭션 수
    READ_COUNT         BIGINT,                            -- 읽은 아이템 수
    FILTER_COUNT       BIGINT,                            -- 필터된 아이템 수
    WRITE_COUNT        BIGINT,                            -- 기록된 아이템 수
    READ_SKIP_COUNT    BIGINT,                            -- 읽기 중 스킵된 아이템 수
    WRITE_SKIP_COUNT   BIGINT,                            -- 쓰기 중 스킵된 아이템 수
    PROCESS_SKIP_COUNT BIGINT,                            -- 처리 중 스킵된 아이템 수
    ROLLBACK_COUNT     BIGINT,                            -- 롤백된 아이템 수
    EXIT_CODE          VARCHAR(20),                       -- 스텝 종료 코드
    EXIT_MESSAGE       VARCHAR(2500),                     -- 스텝 종료 메시지
    LAST_UPDATED       TIMESTAMP,                         -- 마지막 업데이트 시간
    constraint JOB_EXECUTION_STEP_FK foreign key (JOB_EXECUTION_ID)
        references BATCH_JOB_EXECUTION (JOB_EXECUTION_ID) -- 외래키 제약조건
);

-- BATCH_JOB_EXECUTION_CONTEXT Table: Job의 ExecutionContext 관련 정보를 저장하는 테이블
CREATE TABLE BATCH_JOB_EXECUTION_CONTEXT
(
    JOB_EXECUTION_ID   BIGINT PRIMARY KEY,                -- BATCH_JOB_EXECUTION 테이블의 외래키 (유니크한 ID)
    SHORT_CONTEXT      VARCHAR(2500) NOT NULL,            -- 직렬화된 컨텍스트의 짧은 버전
    SERIALIZED_CONTEXT CLOB,                              -- 직렬화된 전체 컨텍스트
    constraint JOB_EXEC_CTX_FK foreign key (JOB_EXECUTION_ID)
        references BATCH_JOB_EXECUTION (JOB_EXECUTION_ID) -- 외래키 제약조건
);

-- BATCH_STEP_EXECUTION_CONTEXT Table: Step의 ExecutionContext 관련 정보를 저장하는 테이블
CREATE TABLE BATCH_STEP_EXECUTION_CONTEXT
(
    STEP_EXECUTION_ID  BIGINT PRIMARY KEY,                  -- BATCH_STEP_EXECUTION 테이블의 외래키 (유니크한 ID)
    SHORT_CONTEXT      VARCHAR(2500) NOT NULL,              -- 직렬화된 컨텍스트의 짧은 버전
    SERIALIZED_CONTEXT CLOB,                                -- 직렬화된 전체 컨텍스트
    constraint STEP_EXEC_CTX_FK foreign key (STEP_EXECUTION_ID)
        references BATCH_STEP_EXECUTION (STEP_EXECUTION_ID) -- 외래키 제약조건
);


CREATE TABLE ORDERS
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(255),
    quantity INT,
    category VARCHAR(50)
);

INSERT INTO ORDERS (name, quantity, category)
SELECT CONCAT('Order ', id), FLOOR(RAND() * 100), 'BOOK'
FROM (SELECT @id := @id + 1 AS id FROM information_schema.tables, (SELECT @id := 0) temp) temp2 LIMIT 1000000;
