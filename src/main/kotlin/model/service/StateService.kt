package model.service


import model.domain.DialogState
import model.domain.DialogState.DIALOG
import model.repository.StateRepository
import model.repository.impl.MariaDbStateRepositoryImpl

class StateService {
    private val stateRepository: StateRepository = MariaDbStateRepositoryImpl()

    fun getState(userId: Int): DialogState {
        var state = stateRepository.find(userId)
        if (state == null) {
            stateRepository.save(userId, DIALOG)
            state = DIALOG
        }
        return state
    }

    fun saveState(userId: Int, state: DialogState) {
        stateRepository.save(userId, state)
    }
}