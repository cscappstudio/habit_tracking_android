package com.cscmobi.habittrackingandroid.presentation.ui.activity

import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.base.BaseActivity
import com.cscmobi.habittrackingandroid.data.model.FreeIAP
import com.cscmobi.habittrackingandroid.databinding.ActivityMainBinding
import com.cscmobi.habittrackingandroid.presentation.ui.view.HomeFragment
import com.cscmobi.habittrackingandroid.thanhlv.database.AppDatabase
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import com.cscmobi.habittrackingandroid.thanhlv.ui.ChallengeFragment
import com.cscmobi.habittrackingandroid.thanhlv.ui.ProfileFragment
import com.cscmobi.habittrackingandroid.thanhlv.ui.ProgressFragment
import com.cscmobi.habittrackingandroid.utils.AlarmUtils
import com.cscmobi.habittrackingandroid.utils.Constant
import com.cscmobi.habittrackingandroid.utils.Helper
import com.cscmobi.habittrackingandroid.utils.Helper.freeIAP
import com.cscmobi.habittrackingandroid.utils.Helper.getMySharedPreferences
import com.cscmobi.habittrackingandroid.utils.NotifiTask
import com.cscmobi.habittrackingandroid.utils.Utils
import com.cscmobi.habittrackingandroid.utils.Utils.toDate
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.thanhlv.ads.lib.AdMobUtils
import com.thanhlv.fw.spf.SPF
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val fragment1 = HomeFragment()
    private val fragment2 = ProgressFragment()
    private val fragment3 = ChallengeFragment()
    private val fragment4 = ProfileFragment()

    private val appDatabase: AppDatabase by inject()
    var taskFilter = mutableListOf<Task>()
    private var isSetUpAlarm = false
    private var taskSize = 0
    override fun getLayoutRes(): Int {
        return R.layout.activity_main
    }


    fun initNotifiTask() {

        lifecycleScope.launch {
            if (NotifiTask.db == null)
                NotifiTask.db = appDatabase

            NotifiTask.setUpWorker(this@MainActivity, this@MainActivity)

        }

    }

    fun initAlarm() {
        lifecycleScope.launch {
            if (isSetUpAlarm) return@launch

            if (NotifiTask.db == null)
                NotifiTask.db = appDatabase
            NotifiTask.db?.dao()?.getAllTask()?.collect {
                taskSize = it.size
                var task = it.filter { Helper.validateTask(it, Helper.currentDate.toDate()) }
                if (task.isNullOrEmpty()) return@collect
                taskFilter.clear()
                task.forEach { t ->
                    t.remind?.let {
                        if (it.isOpen == true) {
                            taskFilter.add(t)
                        }
                    }
                }
                AlarmUtils.createNotificationChannel(this@MainActivity)
                AlarmUtils.create(this@MainActivity, taskFilter)
                isSetUpAlarm = true
            }
        }
    }

    override fun initView() {
        binding.bottomNavigationView.background = null
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
            .build()



        binding.bottomNavigationView.setOnNavigationItemSelectedListener(listener);

        initAlarm()

        freeIAP = FreeIAP.fromJson(this)


        with(getMySharedPreferences()) {
            if (this.getLong("currentDDay", -1L) != Helper.currentDate.toDate()) {
                this.edit().putLong("currentDDay", Helper.currentDate.toDate()).apply()
                this.edit().putBoolean("isDialogCongraShown2", false).apply()
                Helper.isNewDay = true
            }
        }

        if (freeIAP.week != Helper.getCurrentWeek()) {
            freeIAP.week = Helper.getCurrentWeek()
            freeIAP.isSkip = false
        }
    }

    override fun setEvent() {
        binding.fab.setOnClickListener {
            if (!SPF.isProApp(this)) {
                    if (taskSize >= Constant.FREEMAXTASK
                    ) {
                        val getReward = freeIAP.rewardTimes
                        if (getReward >= Constant.MAXGETREWARD) {
                            Toast.makeText(this@MainActivity, "go to premium", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            AdMobUtils.createRewardAds(
                                this@MainActivity,
                                getString(R.string.rewardsAdsId),
                                object : AdMobUtils.Companion.LoadAdCallback {
                                    override fun onLoaded(ad: Any?) {
                                        AdMobUtils.showRewardAds(this@MainActivity, object :
                                            FullScreenContentCallback() {
                                            override fun onAdDismissedFullScreenContent() {
                                                super.onAdDismissedFullScreenContent()
                                                freeIAP.rewardTimes++

                                                startActivity(
                                                    Intent(
                                                        this@MainActivity,
                                                        NewHabitActivity::class.java
                                                    )
                                                )

                                            }
                                        })
                                    }

                                    override fun onLoadFailed() {
                                    }

                                })
                        }
                    } else {
                        startActivity(Intent(this@MainActivity, NewHabitActivity::class.java))
                    }


            } else
                startActivity(Intent(this, NewHabitActivity::class.java))
        }
    }

    fun showFragment(fmShow: Fragment?) {
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


    override fun onStop() {
        super.onStop()
        initNotifiTask()
        isSetUpAlarm = false
        freeIAP.saveToPreference(this)

    }


}