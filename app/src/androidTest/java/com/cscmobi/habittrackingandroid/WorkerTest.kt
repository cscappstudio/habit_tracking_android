package com.cscmobi.habittrackingandroid

import android.content.Context
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.testing.WorkManagerTestInitHelper
import androidx.work.workDataOf
import com.cscmobi.habittrackingandroid.utils.NotifiTask
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
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
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var workerManagerTestRule = WorkManagerTestRule()
    private lateinit var context: Context
    private lateinit var executor: Executor

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        executor = Executors.newSingleThreadExecutor()
    }

    @Test
    fun testPeriodicSampleWorker() {
        val inputData = workDataOf("Worker" to "sampleWorker")
        // 1
        val request = PeriodicWorkRequestBuilder<NotifiTask.NotifiTaskWorker>(1, TimeUnit.HOURS)
            .setInputData(inputData)
            .build()
        // 2
        val testDriver = WorkManagerTestInitHelper
            .getTestDriver(workerManagerTestRule.targetContext)
        val workManager = workerManagerTestRule.workManager
        // 3
        workManager.enqueue(request).result.get()
        // 4
        testDriver?.setPeriodDelayMet(request.id)
        // 5
        val workInfo = workManager.getWorkInfoById(request.id).get()

        // 6
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

    class WorkManagerTestRule : TestWatcher() {
        lateinit var targetContext: Context
        lateinit var testContext: Context
        lateinit var configuration: Configuration
        lateinit var workManager: WorkManager

        override fun starting(description: Description?) {
            targetContext = InstrumentationRegistry.getInstrumentation().targetContext
            testContext = InstrumentationRegistry.getInstrumentation().context
            configuration = Configuration.Builder()
                // Set log level to Log.DEBUG to make it easier to debug
                .setMinimumLoggingLevel(Log.DEBUG)
                // Use a SynchronousExecutor here to make it easier to write tests
                .setExecutor(SynchronousExecutor())
                .build()

            // Initialize WorkManager for instrumentation tests.
            WorkManagerTestInitHelper.initializeTestWorkManager(targetContext, configuration)
            workManager = WorkManager.getInstance(targetContext)
        }
    }
}