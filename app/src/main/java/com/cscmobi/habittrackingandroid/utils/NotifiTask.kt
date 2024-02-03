package com.cscmobi.habittrackingandroid.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.cscmobi.habittrackingandroid.data.model.TaskInDay
import com.cscmobi.habittrackingandroid.thanhlv.database.AppDatabase
import com.cscmobi.habittrackingandroid.thanhlv.model.History
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


object NotifiTask {

    var db: AppDatabase? = null
    var tasks = mutableListOf<Task>()


    fun setUpWorker(owner: LifecycleOwner, context: Context) {
        // need to check task change then add requestwork again
        if (tasks.isEmpty()) return

        val uniqueWorkName = Constant.WORKNAME

        val uploadWorkRequest: PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<NotifiTaskWorker>(1, TimeUnit.DAYS)
                .build()


        val workManager = WorkManager.getInstance(context)
        val workQuery = workManager.getWorkInfosForUniqueWorkLiveData(uniqueWorkName)



        workQuery.observe(owner) { workInfos ->
            if (workInfos.isNullOrEmpty()) {
                // Work is not enqueued, so enqueue it
                workManager.enqueueUniquePeriodicWork(
                    uniqueWorkName,
                    ExistingPeriodicWorkPolicy.UPDATE,
                    uploadWorkRequest
                )
            } else {
                // Work is already enqueued or running, do nothing or handle as needed
            }
        }
    }

    class NotifiTaskWorker(val appContext: Context, workerParams: WorkerParameters) :
        CoroutineWorker(appContext, workerParams) {

        override suspend fun doWork(): Result {
            try {
                Log.d("Worker", "worker run")
                insertHistoryifNotExit()
                var taskFilter = mutableListOf<Task>()
                taskFilter.clear()

                tasks.forEach { task ->
                    task.remind?.let {
                        if (it.isOpen == true) {
                            tasks.add(task)
                        }
                    }
                }
                AlarmUtils.createNotificationChannel(appContext)
                AlarmUtils.create(appContext, taskFilter)
            } catch (e: Exception) {
                return Result.failure()
            }
            return Result.success()
        }


        suspend fun insertHistoryifNotExit() {
            var date = Utils.getCurrentCalenderWithoutHour().time.time
            var history = History()
            history.date = date
            history.taskInDay = tasks.map { TaskInDay(it.id) }
            db?.dao()?.getHistorybyDate(date)?.collect {
                if (it == null) {
                    db?.dao()?.insertHistory(
                        history.copy()
                    )

                }

            }

        }


    }
}
