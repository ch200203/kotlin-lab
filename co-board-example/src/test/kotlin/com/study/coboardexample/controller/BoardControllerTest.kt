package com.study.coboardexample.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
class BoardControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `POST request with @RequestBody should bind data correctly`() {
        val jsonContent = """
            {
                "title": "Sample Title",
                "content": "Sample Content"
            }
        """.trimIndent()

        mockMvc.post("/board/create") {
            contentType = MediaType.APPLICATION_JSON
            content = jsonContent
        }
            .andExpect {
                status { isOk() }
                content { string("1") }  // 적절한 응답 값 설정
            }
    }

    @Test
    fun `GET request with @ModelAttribute should bind data correctly`() {
        mockMvc.get("/board/create") {
            param("title", "Sample Title")
            param("content", "Sample Content")
        }
            .andExpect {
                status { isOk() }
                content { string("GET BoardRequest: BoardRequest(title=Sample Title, content=Sample Content)") }
            }
    }

    @Test
    fun `GET request with @ParameterObject should not bind data when no @Setter`() {
        mockMvc.get("/board/info") {
            param("title", "Sample Title")
            param("content", "Sample Content")
        }
            .andExpect {
                status { isOk() }
                content { string("BoardResponse(title=null, content=null)") }  // `null`로 바인딩되지 않는 경우 확인
            }
    }
}
