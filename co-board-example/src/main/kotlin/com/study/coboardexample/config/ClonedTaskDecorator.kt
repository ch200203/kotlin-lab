package com.study.coboardexample.config

import org.slf4j.MDC
import org.springframework.core.task.TaskDecorator

/**
 * 비동기 처리를 위해 TaskExecutor 를 활용하기 시작하면
 * 해당 executor 는 새로운 Thread 를 생성하게 되는데 이 쓰레드에는 기존 쓰레드의 context 가 넘어가지 않고 새로 만들어지기 때문에
 * 다 빈값으로 나오게 됩니다.
 *
 * 스레드에 저장된 정보가 새로운 비동기 쓰레드에 전달 안됨 → 스레드 context 활용하는 MDCUtil 을 이용해 처리하는 모든 로깅이나 로직이 먹통이 됨.
 *
 * TaskDecorator 를 이용해서 비동기처리하는 taskExecutor 생성시 커스터마이징
 * 기존 스레드의 MDC 전체값을 카피해서 넣어 버리는 형태인데 당연히 기존 스레드와 분리 되어 있어서 값만 일치 할뿐 서로 연결되지 않음
 */
class ClonedTaskDecorator : TaskDecorator {

    override fun decorate(task: Runnable): Runnable {
        val callerThreadContext = MDC.getCopyOfContextMap()

        return Runnable {
            MDC.setContextMap(callerThreadContext)
            task.run()
        }
    }
}
