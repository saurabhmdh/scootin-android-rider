package com.scootin.services

import android.app.Application
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.scootin.database.dao.CacheDao
import com.scootin.di.IWorkerFactory
import com.scootin.network.AppExecutors
import com.scootin.util.constants.AppConstants
import kotlinx.coroutines.coroutineScope
import timber.log.Timber
import javax.inject.Inject

class StartupDataBaseWorker (
    private val context: Application,
    params: WorkerParameters,
    val cacheDao: CacheDao
): CoroutineWorker(context, params) {
    override suspend fun doWork(): Result = coroutineScope {
        cacheDao.deleteCache(AppConstants.FCM_ID)
        Timber.i("Running  ..")
        Result.success()
    }


    /* Factory implementation for Work Manager */
    class Factory @Inject constructor(
        val appExecutors: AppExecutors,
        val application: Application,
        val cacheDao: CacheDao
    )
        : IWorkerFactory<StartupDataBaseWorker> {

        override fun create(params: WorkerParameters): StartupDataBaseWorker {
            return StartupDataBaseWorker(application, params, cacheDao)
        }
    }
}