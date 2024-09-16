package com.study.kopringbatch

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.InitializingBean

class GreetingTask : Tasklet, InitializingBean {
    private val log = KotlinLogging.logger {}

    // Tasklet은 execute 메소드를 구현이 필요
    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus? {
        log.info { "------------------ Task Execute -----------------" };
        log.info {
            "GreetingTask: $contribution, $chunkContext";
        }

        /**
         * 최종적으로 RepeatStatus 를 반환하며 이 값은 다음과 같다.
         * FINISHED: 태스크릿이 종료되었음을 나타낸다.
         * CONTINUABLE: 계속해서 태스크를 수행하도록한다.
         * continueIf(condition): 조건에 따라 종료할지 지속할지 결정하는 메소드에 따라 종료/지속을 결정한다
         */
        return RepeatStatus.FINISHED
    }

    // afterPropertiesSet: 태스크를 수해할때 프로퍼티를 설정하고 난 뒤에 수행되는 메소드이다.
    // 사실상 없어도 된다.
    override fun afterPropertiesSet() {
        log.info { "----------------- After Properites Sets() --------------" };
    }
}
