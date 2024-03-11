package com.cscmobi.habittrackingandroid.presentation.ui.view

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.base.BaseBindingAdapter
import com.cscmobi.habittrackingandroid.base.BaseFragment
import com.cscmobi.habittrackingandroid.data.model.HabitCollection
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import com.cscmobi.habittrackingandroid.databinding.FragmentDetailCollectionBinding
import com.cscmobi.habittrackingandroid.presentation.ItemBaseListener
import com.cscmobi.habittrackingandroid.presentation.ItemDetailCollection
import com.cscmobi.habittrackingandroid.presentation.ui.activity.NewHabitActivity
import com.cscmobi.habittrackingandroid.presentation.ui.intent.CollectionIntent
import com.cscmobi.habittrackingandroid.presentation.ui.viewmodel.CollectionViewModel
import com.cscmobi.habittrackingandroid.presentation.ui.viewstate.CollectionState
import com.cscmobi.habittrackingandroid.thanhlv.ui.SubscriptionsActivity
import com.cscmobi.habittrackingandroid.utils.Constant
import com.cscmobi.habittrackingandroid.utils.CustomEditMenu
import com.cscmobi.habittrackingandroid.utils.DialogUtils
import com.cscmobi.habittrackingandroid.utils.Helper
import com.cscmobi.habittrackingandroid.utils.Utils
import com.cscmobi.habittrackingandroid.utils.setDrawableString
import com.google.android.gms.ads.FullScreenContentCallback
import com.thanhlv.ads.lib.AdMobUtils
import com.thanhlv.fw.spf.SPF
import kotlinx.coroutines.launch
import java.util.Calendar

class DetailCollectionFragment :
    BaseFragment<FragmentDetailCollectionBinding>(FragmentDetailCollectionBinding::inflate) {
    lateinit var detailCollectionAdapter: BaseBindingAdapter<Task>
    private val collectionViewModel by activityViewModels<CollectionViewModel>()
    private var currentCollection: HabitCollection? = null

    @SuppressLint("SetTextI18n")
    override fun initView(view: View) {
        lifecycleScope.launch {
            collectionViewModel.state.collect {
                when (it) {
                    is CollectionState.Collection -> {
                        binding.txtCollection.text = try {
                            val resourceValue = binding.root.context.getString(it.data.name.toInt())
                            it.data.name = resourceValue
                            resourceValue
                        } catch (e: Exception) {
                            it.data.name = it.data.name
                            it.data.name
                        }
                        binding.txtNumberTask.text =
                            "${it.data.task!!.size.toString()} ${getString(R.string.habits)}"
                        binding.ivCollection.setDrawableString(it.data.image!!)
                        initAdapter(it.data.task as ArrayList<Task>)
                        if (it.data.isEdit) binding.ivEdit.visibility =
                            View.VISIBLE else binding.ivEdit.visibility = View.GONE
                        currentCollection = it.data
                    }

                    else -> {}
                }
            }

        }

        if (Utils.isShowAds(requireContext())) {
            binding.adView.visibility = View.VISIBLE
            AdMobUtils.createBanner(
                requireContext(),
                binding.root.context.getString(R.string.admob_banner_id),
                AdMobUtils.BANNER_COLLAPSIBLE_BOTTOM,
                binding.adView,
                object : AdMobUtils.Companion.LoadAdCallback {
                    override fun onLoaded(ad: Any?) {
                    }

                    override fun onLoadFailed() {
                        binding.adView.visibility = View.GONE
                    }
                })
        }
    }

    private fun initAdapter(list: ArrayList<Task>) {
        list.forEach {
            it.name = try {
                val resourceValue = binding.root.context.getString(it.name.toInt())
                resourceValue
            } catch (e: Exception) {
                it.name
            }
        }

        detailCollectionAdapter = BaseBindingAdapter(
            R.layout.item_detail_collection,
            layoutInflater,
            object : DiffUtil.ItemCallback<Task>() {
                override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                    return oldItem.name == newItem.name
                }

                override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                    return oldItem == newItem
                }
            }
        )
        detailCollectionAdapter.setListener(object : ItemDetailCollection<Task> {
            override fun onItemClicked(item: Task) {
                (requireActivity() as NewHabitActivity).let {
                    if (item.notBelongDefaultCollection)
                        it.newHabitFragment.newHabitFragmentState =
                            NewHabitFragment.NewHabitFragmentState.ADDTOROUTINEWITHCOLLECTION
                    else
                        it.newHabitFragment.newHabitFragmentState =
                            NewHabitFragment.NewHabitFragmentState.ADDTOROUTINE
                    it.addFragmentNotHide(it.newHabitFragment, NewHabitFragment.TAG)

                    lifecycleScope.launch {
                        collectionViewModel.userIntent.send(
                            CollectionIntent.PassTaskfromCollection(
                                item.copy()
                            )
                        )

                    }
                }
            }

            override fun onAddTask(item: Task) {

                if (!SPF.isProApp(requireContext())) {
                    if ((requireActivity() as NewHabitActivity).taskSize >= Constant.FREEMAXTASK
                    ) {
                        val getReward = Helper.freeIAP.rewardTimes
                        if (getReward >= Constant.MAXGETREWARD) {
                            val intent =
                                Intent(requireActivity(), SubscriptionsActivity::class.java)
                            startActivity(intent)
                        } else {

                            DialogUtils.showWatchAdsDialog(
                                requireActivity(),
                                Constant.MAXGETREWARD.minus(getReward),
                                "",
                                ""
                            ) {
                                insertTask(item)
                            }

                        }
                    } else {
                        insertTask(item)
                    }

                } else {
                    insertTask(item)
                }
            }


        })
        binding.adapter = detailCollectionAdapter

        detailCollectionAdapter.setData(list)

    }

    private fun insertTask(item: Task) {
        lifecycleScope.launch {
            var clone = item.copy(startDate = Calendar.getInstance().time.time)
            collectionViewModel.userIntent.send(
                CollectionIntent.CreateTaskToRoutine(
                    clone
                )
            )

            Toast.makeText(requireContext(), "Create task success", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun setEvent() {
        binding.layoutHeader.ivBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.ivEdit.setOnClickListener {
            setPopUpWindow(it)
        }
    }

    fun setPopUpWindow(v: View) {
        val popup = CustomEditMenu(requireContext(), {
            currentCollection?.let { collection ->
                collectionViewModel.setStateUpdateCollection(collection)

                (requireActivity() as NewHabitActivity).let {

                    it.addFragmentNotHide(it.createCollectionFragment, CreateCollectionFragment.TAG)
                }
            }


        }, {
            lifecycleScope.launch {
                currentCollection?.let {
                    collectionViewModel.userIntent.send(CollectionIntent.DeleteCollection(it))
                    parentFragmentManager.popBackStack()
                }

            }
        })
        popup.showAsDropDown(v)

    }
}