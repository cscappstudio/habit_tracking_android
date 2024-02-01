package com.cscmobi.habittrackingandroid.presentation.ui.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.base.BaseActivity
import com.cscmobi.habittrackingandroid.data.model.RemindTask
import com.cscmobi.habittrackingandroid.databinding.ActivityMainBinding
import com.cscmobi.habittrackingandroid.presentation.ui.view.HomeFragment
import com.cscmobi.habittrackingandroid.presentation.ui.viewstate.HomeState
import com.cscmobi.habittrackingandroid.thanhlv.database.AppDatabase
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import com.cscmobi.habittrackingandroid.thanhlv.ui.ChallengeFragment
import com.cscmobi.habittrackingandroid.thanhlv.ui.ProfileFragment
import com.cscmobi.habittrackingandroid.thanhlv.ui.ProgressFragment
import com.cscmobi.habittrackingandroid.utils.AlarmUtils
import com.cscmobi.habittrackingandroid.utils.Constant
import com.cscmobi.habittrackingandroid.utils.Helper
import com.cscmobi.habittrackingandroid.utils.NotifiTask
import com.cscmobi.habittrackingandroid.utils.Utils
import com.cscmobi.habittrackingandroid.utils.Utils.toDate
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.thanhlv.fw.spf.SPF
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit


class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val fragment1 = HomeFragment()
    private val fragment2 = ProgressFragment()
    private val fragment3 = ChallengeFragment()
    private val fragment4 = ProfileFragment()

    private val appDatabase: AppDatabase by inject()


    override fun getLayoutRes(): Int {
        return R.layout.activity_main
    }


    fun initNotifiTask() {
        NotifiTask.db = appDatabase

        lifecycleScope.launch {
            NotifiTask.db?.dao()?.getAll()?.collect {
                try {
                    var taskFilter =
                        it.filter { Helper.validateTask(it, Helper.currentDate.toDate()) }
                    Log.d("Mainaaaaaaaaaaaaaa", taskFilter.toString())

                    NotifiTask.tasks = taskFilter.toMutableList()
                } catch (e: Exception) {
                    HomeState.Tasks(arrayListOf())
                }

            }

        }


//        AlarmUtils.createNotificationChannel(this)

//        var listTask = mutableListOf<Task>()
//        listTask.add(Task(name = "111111", remind = RemindTask(true,4,38,"PM")))
//        listTask.add(Task(name = "222222", remind = RemindTask(true,4,39,"PM")))
//        listTask.add(Task(name = "33333", remind = RemindTask(true,4,40,"PM")))
//        listTask.add(Task(name = "44444", remind = RemindTask(true,4,41,"PM")))
//
//        AlarmUtils.create(this,listTask)
    }

    override fun initView() {
        initNotifiTask()
        binding.bottomNavigationView.background = null
//        binding.bottomNavigationView.menu.getItem(2).isEnabled = false

        initFragments()
        showFragment(fragment1)

        val listener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_1 -> {
                    showFragment(fragment1)
                    return@OnNavigationItemSelectedListener true
                }

                R.id.page_2 -> {
                    showFragment(fragment2)
                    return@OnNavigationItemSelectedListener true
                }

                R.id.page_3 -> {
                    showFragment(fragment3)
                    return@OnNavigationItemSelectedListener true
                }

                R.id.page_4 -> {
                    showFragment(fragment4)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false


        }

        val shapeDrawable: MaterialShapeDrawable =
            binding.bottomAppBar.background as MaterialShapeDrawable
        shapeDrawable.shapeAppearanceModel = shapeDrawable.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, Utils.dpToPx(20f, this).toFloat())
            .setTopRightCorner(CornerFamily.ROUNDED, Utils.dpToPx(20f, this).toFloat())
//            .setBottomRightCorner(CornerFamily.ROUNDED, DisplayUtils.dpToPx(30f).toFloat())
//            .setBottomLeftCorner(CornerFamily.ROUNDED, DisplayUtils.dpToPx(30f).toFloat())
            .build()



        binding.bottomNavigationView.setOnNavigationItemSelectedListener(listener);

    }

    override fun setEvent() {
        binding.fab.setOnClickListener {
            startActivity(Intent(this, NewHabitActivity::class.java))
        }
    }

    fun showFragment(fmShow: Fragment?) {

//        transaction.setCustomAnimations(
//            R.anim.bottom_up,
//            R.anim.bottom_down
//        )

        val transaction: FragmentTransaction = supportFragmentManager
            .beginTransaction()

        transaction.hide(fragment1)
        transaction.hide(fragment2)
        transaction.hide(fragment3)
        transaction.hide(fragment4)

        fmShow?.let {

            transaction.show(it)
        }
        transaction.commit()
    }

    private fun initFragments() {
        addFragment(R.id.frame_container, fragment1, "fragment1")
        addFragment(R.id.frame_container, fragment2, "fragment2")
        addFragment(R.id.frame_container, fragment3, "fragment3")
        addFragment(R.id.frame_container, fragment4, "fragment4")

        if (SPF.isFirstOpenApp(this)) SPF.setFirstOpenApp(this, false)
    }

    override fun onPause() {
        super.onPause()
        NotifiTask.setUpWorker(this@MainActivity,this@MainActivity)

    }
    override fun onDestroy() {
        super.onDestroy()

    }


}