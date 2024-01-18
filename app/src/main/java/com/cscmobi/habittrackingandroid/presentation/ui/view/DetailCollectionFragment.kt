package com.cscmobi.habittrackingandroid.presentation.ui.view

import android.annotation.SuppressLint
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.base.BaseBindingAdapter
import com.cscmobi.habittrackingandroid.base.BaseFragment
import com.cscmobi.habittrackingandroid.data.model.Task
import com.cscmobi.habittrackingandroid.databinding.FragmentDetailCollectionBinding
import com.cscmobi.habittrackingandroid.presentation.ui.viewmodel.CollectionViewModel
import com.cscmobi.habittrackingandroid.presentation.ui.viewstate.CollectionState
import kotlinx.coroutines.launch

class DetailCollectionFragment :
    BaseFragment<FragmentDetailCollectionBinding>(FragmentDetailCollectionBinding::inflate) {
    lateinit var detailCollectionAdapter: BaseBindingAdapter<Task>
    private val collectionViewModel  by activityViewModels<CollectionViewModel>()

    @SuppressLint("SetTextI18n")
    override fun initView(view: View) {
        lifecycleScope.launch {
            collectionViewModel.state.collect {
                when (it) {
                    is CollectionState.Collection -> {
                        binding.txtCollection.text = it.data.name
                        binding.txtNumberTask.text = "${it.data.task.size.toString()} habits"
                        binding.ivCollection.setImageResource(it.data.image)
                        initAdapter(it.data.task as ArrayList<Task>)

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
        binding.adapter = detailCollectionAdapter

        detailCollectionAdapter.setData(list)

    }

    override fun setEvent() {
    }
}