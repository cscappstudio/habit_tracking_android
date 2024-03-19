package com.cscmobi.habittrackingandroid.presentation.ui.activity

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.base.BaseActivity
import com.cscmobi.habittrackingandroid.databinding.ActivityNewhabitBinding
import com.cscmobi.habittrackingandroid.presentation.ui.intent.CollectionIntent
import com.cscmobi.habittrackingandroid.presentation.ui.view.CollectionFragment
import com.cscmobi.habittrackingandroid.presentation.ui.view.CreateCollectionFragment
import com.cscmobi.habittrackingandroid.presentation.ui.view.NewHabitFragment
import com.cscmobi.habittrackingandroid.presentation.ui.viewmodel.CollectionViewModel
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import com.cscmobi.habittrackingandroid.utils.Constant
import com.cscmobi.habittrackingandroid.utils.Helper
import com.cscmobi.habittrackingandroid.utils.ObjectWrapperForBinder
import com.google.android.gms.ads.FullScreenContentCallback
import com.thanhlv.ads.lib.AdMobUtils
import com.thanhlv.fw.spf.SPF
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewHabitActivity : BaseActivity<ActivityNewhabitBinding>(),
    NewHabitFragment.INewHabitListener {
    private val collectionViewModel: CollectionViewModel by viewModel()
//    private var isLoadInterAds = false
    private var isRewardLoad = false
    var taskSize:Int = -1

    override fun getLayoutRes(): Int {
        return R.layout.activity_newhabit
    }

    val collectionFragment = CollectionFragment()
    val newHabitFragment = NewHabitFragment()
    val createCollectionFragment = CreateCollectionFragment()
//    val bottomsheetNewHabitFragment = BottomsheetNewHabitFragment()

    override fun initView() {
        collectionViewModel.setUp()
        addFragment(R.id.fr_container, collectionFragment, "collectionFragment")

        if (!SPF.isProApp(this)) {
            AdMobUtils.createInterstitialAd(
                this@NewHabitActivity,
                getString(R.string.inter_id),
                object : AdMobUtils.Companion.LoadAdCallback {
                    override fun onLoaded(ad: Any?) {
//                        isLoadInterAds = true
                    }

                    override fun onLoadFailed() {
//                        isLoadInterAds = false
                    }
                })
        }


        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (collectionFragment.isVisible) {
                    if (AdMobUtils.interstitialAdAlready(this@NewHabitActivity)
                        && !SPF.isProApp(this@NewHabitActivity))
                        AdMobUtils.showInterstitialAd(this@NewHabitActivity,
                            object : FullScreenContentCallback() {
                                override fun onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent()
                                    finish()
                                }
                            })
                    else finish()
                } else {
                    supportFragmentManager.popBackStack()
                }
            }
        })

        lifecycleScope.launch {
            collectionViewModel.taskSize.collect{
                taskSize = it
                if (it > 5 && !isRewardLoad) {
                    AdMobUtils.createRewardAds(
                        this@NewHabitActivity,
                        getString(R.string.rewardsAdsId),
                        object : AdMobUtils.Companion.LoadAdCallback {
                            override fun onLoaded(ad: Any?) {
                                isRewardLoad = true
                            }

                            override fun onLoadFailed() {
                                isRewardLoad = false
                            }

                        })
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        intent.extras?.let {
            val dataTask = it.getBinder(Constant.EditTask) as? ObjectWrapperForBinder
            dataTask?.let { task ->
                replaceFragmentNotToBackStack(
                    R.id.fr_container,
                    newHabitFragment,
                    NewHabitFragment.TAG
                )
                lifecycleScope.launch {
                    collectionViewModel.userIntent.send(CollectionIntent.EditTask(task.data as Task))

                }
            }
        }
    }

    fun replaceFragment(fr: Fragment, tagFragment: String) {
        if (!fr.isAdded)
            replaceFragment(R.id.fr_container, fr, tagFragment)
    }

    fun replaceFragmentNotToBackStack(fr: Fragment, tagFragment: String) {
        if (!fr.isAdded)
            replaceFragmentNotToBackStack(R.id.fr_container, fr, tagFragment)

    }

    fun addFragmentNotHide(fr: Fragment, tagFragment: String) {
        if (!fr.isAdded)
            addFragmentNotHide(R.id.fr_container, fr, tagFragment)

    }

    override fun setEvent() {
    }

    override fun addTask(task: Task) {
        createCollectionFragment.addTask(task)
    }

    override fun editTaskCollection(task: Task) {
        createCollectionFragment.editTask(task)
    }


}