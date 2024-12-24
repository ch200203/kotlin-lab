package com.study.coboardexample.config

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.MDC
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.util.UUID

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class MDCLoggingFilter : Filter {


    /**
     * MSA 와 같은 분산환경에서는 로그추적이 어려워 spring-cloud-sleuth 와 같은 라이브러리를 종종 사용한다.
     */
    override fun doFilter(servletRequest: ServletRequest?, servletResponse: ServletResponse?, filterChain: FilterChain?) {
        val uuid = UUID.randomUUID()
        MDC.put("uuid", uuid.toString())
        filterChain?.doFilter(servletRequest, servletResponse)
        // 반드시 clear 를 호출해주어야한다.
        // Thread Pool 에 있는 스레드 저장소에 uuid 정보를 넣어놓고 반납한다.
        // 이때 반납된 스레드를 재사용할때 이전 데이터가 남아있을 수 있기 때문에, 반드시 clear 를 해줘야한다.
        MDC.clear()
    }

    /**
     * nginx 사용시 $reuqestId 를 받아서 사용
     */
    /*override fun doFilter(servletRequest: ServletRequest?, servletResponse: ServletResponse?, filterChain: FilterChain?) {
        val requestId = (servletRequest as HttpServletRequest).getHeader("X-RequestID")
        MDC.put("request_id", requestId?.takeIf { it.isNotEmpty() } ?: UUID.randomUUID().toString().replace("-", ""))
        filterChain?.doFilter(servletRequest, servletResponse)
        MDC.clear()
    }*/

}