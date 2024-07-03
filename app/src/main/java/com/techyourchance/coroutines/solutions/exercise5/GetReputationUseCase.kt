package com.techyourchance.coroutines.solutions.exercise5

import com.techyourchance.coroutines.common.ThreadInfoLogger
import com.techyourchance.coroutines.exercises.exercise1.GetReputationEndpoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetReputationUseCase(private val getReputationEndpoint: GetReputationEndpoint) {

     suspend fun getReputationForUser(userId: String) = withContext(Dispatchers.Default) {
         logThreadInfo("getReputationForUser()")
         val reputation = getReputationEndpoint.getReputation(userId)
         logThreadInfo("reputation: $reputation")
         reputation
    }

    private fun logThreadInfo(message: String) {
        ThreadInfoLogger.logThreadInfo(message)
    }

}