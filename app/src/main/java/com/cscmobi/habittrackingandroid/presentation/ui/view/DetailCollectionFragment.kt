package com.cscmobi.habittrackingandroid.presentation.ui.view

import android.annotation.SuppressLint
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
import com.cscmobi.habittrackingandroid.utils.CustomEditMenu
import com.cscmobi.habittrackingandroid.utils.setDrawableString
import kotlinx.coroutines.launch

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
                        binding.txtCollection.text = it.data.name
                        binding.txtNumberTask.text = "${it.data.task!!.size.toString()} habits"
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

    }

    private fun initAdapter(list: ArrayList<Task>) {
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
                    it.newHabitFragment.newHabitFragmentState =
                        NewHabitFragment.NewHabitFragmentState.ADDTOROUTINE
                    it.addFragmentNotHide(it.newHabitFragment, NewHabitFragment.TAG)
                    lifecycleScope.launch {
                        collectionViewModel.userIntent.send(
                            CollectionIntent.PassTaskfromCollection(
                                item
                            )
                        )

                    }
                }
            }

            override fun onAddTask(item: Task) {
                lifecycleScope.launch {
                    var clone = item.copy()
                    collectionViewModel.userIntent.send(CollectionIntent.CreateTaskToRoutine(clone))
                    Toast.makeText(requireContext(), "Create task success", Toast.LENGTH_SHORT).show()
                }

            }

        })
        binding.adapter = detailCollectionAdapter

        detailCollectionAdapter.setData(list)

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

                    it.replaceFragment(it.createCollectionFragment, CreateCollectionFragment.TAG)
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