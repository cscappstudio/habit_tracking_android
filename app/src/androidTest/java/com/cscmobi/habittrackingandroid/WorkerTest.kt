package com.cscmobi.habittrackingandroid

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.testing.WorkManagerTestInitHelper
import androidx.work.workDataOf
import com.cscmobi.habittrackingandroid.utils.NotifiTask
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class BasicInstrumentationTest {

//    @get:Rule
//    val workerTestRule = TestListenableWorkerBuilder<NotifiTask.NotifiTaskWorker>(context = InstrumentationRegistry.getInstrumentation().targetContext)
//        .setInputData(workDataOf(/* your input data here */))
//        .build()
//
//    @Test
//    fun testWorker() {
//        // Get an instance of TestDriver
//        val testDriver = WorkManagerTestInitHelper.getTestDriver(InstrumentationRegistry.getInstrumentation().targetContext)
//
//        // Enqueue your PeriodicWorkRequest
//        val request = PeriodicWorkRequestBuilder<NotifiTask.NotifiTaskWorker>(15, TimeUnit.MINUTES)
//            .build()
//        WorkManager.getInstance(InstrumentationRegistry.getInstrumentation().targetContext).enqueue(request)
//
//        // Drive the Worker forward by setting constraints met
//        testDriver?.setPeriodDelayMet(request.id)
//
//        // Get the WorkInfo for the WorkRequest
//        val workInfo = WorkManager.getInstance(InstrumentationRegistry.getInstrumentation().targetContext).getWorkInfoById(request.id).get()
//
//        // Assert on the WorkInfo or perform your tests
//        assertThat(workInfo.state, `is`(WorkInfo.State.SUCCEEDED))
//    }

    private lateinit var context: Context
    private lateinit var executor: Executor

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        executor = Executors.newSingleThreadExecutor()
    }

    @Test
    fun testPeriodicWork() {
        // Define input data
        val input = workDataOf("KEY_1" to 1, "KEY_2" to 2)

        // Create request
        val request = PeriodicWorkRequestBuilder<NotifiTask.NotifiTaskWorker>(15, TimeUnit.MINUTES)
            .setInputData(input)
            .build()

        val workManager = WorkManager.getInstance(context)
        val testDriver = WorkManagerTestInitHelper.getTestDriver(context)
        // Enqueue and wait for result.
        workManager.enqueue(request).result.get()
        // Tells the testing framework the period delay is met
        testDriver?.setPeriodDelayMet(request.id)
        // Get WorkInfo and outputData
        val workInfo = workManager.getWorkInfoById(request.id).get()

        // Assert
        assertThat(workInfo.state, `is`(WorkInfo.State.ENQUEUED))
    }

    @Test
    fun testWorker() {
        val worker = TestListenableWorkerBuilder<NotifiTask.NotifiTaskWorker>(context).build()
        runBlocking {
            val result = worker.doWork()
            assertThat(result, `is`(ListenableWorker.Result.success()))
        }

//        val result = worker.doWork()
//        assertThat(result, `is`(Result.success()))
    }
}