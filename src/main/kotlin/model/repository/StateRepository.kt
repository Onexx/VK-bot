package model.repository

import model.domain.DialogState

interface StateRepository {
    fun save(userId: Int, state: DialogState)
    fun find(userId: Int): DialogState?
}