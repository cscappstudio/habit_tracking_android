package com.cscmobi.habittrackingandroid.utils

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
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
import com.cscmobi.habittrackingandroid.utils.Utils.toDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit


object NotifiTask {

    var db: AppDatabase? = null
//    var tasks = mutableListOf<Task>()


    fun setUpWorker(owner: LifecycleOwner, context: Context) {

        val uniqueWorkName = Constant.WORKNAME

        val uploadWorkRequest: PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<NotifiTaskWorker>(1, TimeUnit.DAYS)
                .build()

        val workManager = WorkManager.getInstance(context)
        workManager.enqueueUniquePeriodicWork(
            uniqueWorkName,
            ExistingPeriodicWorkPolicy.KEEP,
            uploadWorkRequest
        )
//        val workQuery = workManager.getWorkInfosForUniqueWorkLiveData(uniqueWorkName)
//
//
//
//        workQuery.observe(owner) { workInfos ->
//            if (workInfos.isNullOrEmpty()) {
//                // Work is not enqueued, so enqueue it
//                workManager.enqueueUniquePeriodicWork(
//                    uniqueWorkName,
//                    ExistingPeriodicWorkPolicy.UPDATE,
//                    uploadWorkRequest
//                )
//
//
//            } else {
//                // Work is already enqueued or running, do nothing or handle as needed
//
//                workManager.updateWork(uploadWorkRequest)
//            }
//        }
    }

    fun updateWorkManager() {

    }

    class NotifiTaskWorker(val appContext: Context, workerParams: WorkerParameters) :
        CoroutineWorker(appContext, workerParams) {

        override suspend fun doWork(): Result {
            try {


                db?.dao()?.getAllTask()?.collect {
                    var tasks =
                        it.filter { Helper.validateTask(it, Helper.currentDate.toDate()) }

                    if (tasks.isNullOrEmpty()) return@collect

                    delay(1000L)
                    Log.d("Worker", "1 $tasks")

                    var taskFilter = mutableListOf<Task>()
                    taskFilter.clear()

                    tasks.forEach { t ->
                        t.remind?.let {
                            if (it.isOpen == true) {
                                taskFilter.add(t)
                            }
                        }
                    }
                    Log.d("Worker", "2 $taskFilter")

                    AlarmUtils.createNotificationChannel(appContext)
                    AlarmUtils.create(appContext, taskFilter)

                    withContext(Dispatchers.IO) {
                        Log.d("Worker", "3 insert to history")

                        insertHistoryifNotExit(tasks)
                    }

                }

                return Result.success()
            } catch (e: Exception) {
                return Result.failure()
            }
        }


        suspend fun insertHistoryifNotExit(tasks: List<Task>) {
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
