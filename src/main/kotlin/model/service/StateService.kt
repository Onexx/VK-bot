package model.service

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import model.domain.DialogState
import model.domain.DialogState.DIALOG
import model.repository.StateRepository
import model.repository.impl.MariaDbStateRepositoryImpl
import java.util.concurrent.TimeUnit

class StateService(
    private val stateRepository: StateRepository = MariaDbStateRepositoryImpl()
) {
    private val loader: CacheLoader<Int, DialogState> = CacheLoader.from { userId ->
        var state = stateRepository.find(userId)
        if (state == null) {
            saveState(userId, DIALOG)
            state = DIALOG
        }
        return@from state
    }

    private val cache: LoadingCache<Int, DialogState> = CacheBuilder.newBuilder()
        .weigher { _: Int, value: DialogState -> value.name.length }
        .maximumWeight(10 * 1024 * 1024)
        .expireAfterAccess(5, TimeUnit.MINUTES)
        .build(loader)

    fun getState(userId: Int): DialogState {
        return cache.get(userId)
    }

    fun saveState(userId: Int, state: DialogState) {
        cache.put(userId, state)
        stateRepository.save(userId, state)
    }
}