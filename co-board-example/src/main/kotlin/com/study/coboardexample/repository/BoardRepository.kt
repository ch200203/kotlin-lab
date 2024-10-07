package com.study.coboardexample.repository

import com.study.coboardexample.domain.Board
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BoardRepository: CrudRepository<Board, Long> {

}
