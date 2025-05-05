package no.uio.ifi.in2000_gruppe3.data.log.repository

import no.uio.ifi.in2000_gruppe3.data.database.LogDao

class LogRepository (private val logDao: LogDao) {

    suspend fun addLog(newLog: no.uio.ifi.in2000_gruppe3.data.database.Log) {
        logDao.saveLog(newLog)
    }

    suspend fun deleteLog(logToRemove: no.uio.ifi.in2000_gruppe3.data.database.Log) {
        logDao.deleteLog(logToRemove)
    }

    suspend fun getAllLogs(username: String): List<Int> {
        return logDao.getAllLogs(username)
    }

    suspend fun timesWalked(username: String, hikeId: Int, adjustTimesWalked: Int) {
        logDao.timesWalked(username, hikeId, adjustTimesWalked)
    }

    suspend fun getTimesWalked(username: String, hikeId: Int): Int {
        return logDao.getTimesWalked(username, hikeId)
    }

    suspend fun addNotesToLog(username: String, hikeId: Int, notes: String) {
        logDao.addNotesToLog(username, hikeId, notes)
    }

    suspend fun getNotesForHike(username: String, hikeId: Int): String {
        return logDao.getNotesForHike(username, hikeId)
    }
}