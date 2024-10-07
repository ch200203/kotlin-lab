package com.study.coboardexample.service

import com.study.coboardexample.controller.BoardRequest
import com.study.coboardexample.domain.Board
import com.study.coboardexample.repository.BoardRepository
import kotlinx.coroutines.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BoardService(
    private val boardRepository: BoardRepository
) {

    @Transactional
    fun saveBoard(request: BoardRequest): Long {
        val board = Board(title = request.title, content = request.content)
        val saveBoard = boardRepository.save(board)

        CoroutineScope(Dispatchers.IO).launch {
            delay(1000)
            println("여기서 1초 쉽니다.")
            // 로그 남기기
        }
        return saveBoard.id
    }

}
