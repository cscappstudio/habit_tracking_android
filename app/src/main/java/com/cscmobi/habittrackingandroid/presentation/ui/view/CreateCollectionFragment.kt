package com.cscmobi.habittrackingandroid.presentation.ui.view

import android.content.res.ColorStateList
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.base.BaseBindingAdapter
import com.cscmobi.habittrackingandroid.base.BaseFragment
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import com.cscmobi.habittrackingandroid.data.model.HabitCollection
import com.cscmobi.habittrackingandroid.databinding.FragmentCreateCollectionBinding
import com.cscmobi.habittrackingandroid.presentation.ItemBaseWithPostitionListener
import com.cscmobi.habittrackingandroid.presentation.ui.activity.NewHabitActivity
import com.cscmobi.habittrackingandroid.presentation.ui.intent.CollectionIntent
import com.cscmobi.habittrackingandroid.presentation.ui.viewmodel.CollectionViewModel
import com.cscmobi.habittrackingandroid.presentation.ui.viewstate.CollectionState
import com.cscmobi.habittrackingandroid.utils.Helper
import kotlinx.coroutines.launch

class CreateCollectionFragment: BaseFragment<FragmentCreateCollectionBinding>(FragmentCreateCollectionBinding::inflate) {

    private var hadChangeState = false
    private val collectionViewModel by activityViewModels<CollectionViewModel>()
    private val bottomSheetFragment = BottomSheetCollectionFragment()
    private var newTasks = mutableListOf<Task>()
    private var taskCollectionAdapter: BaseBindingAdapter<Task>? = null

    var collectionData: HabitCollection = HabitCollection()
    companion object {
        val TAG = "CreateCollectionFragment"
    }
    override fun initView(view: View) {
        initTaskCollectionAdapter()
        lifecycleScope.launch {
            collectionViewModel.userIntent.send(CollectionIntent.NotCreateCollection)
        }

        lifecycleScope.launch {
            collectionViewModel.state.collect{
                if (it is CollectionState.IdleCreateCollection) {
                    setEmptyCollection()

                } else if (it is CollectionState.CreateCollection) {
                    //TODO insert to collection
                }

            }
        }

        bottomSheetFragment.listener = object : BottomSheetCollectionFragment.IBottomCollection {
            override fun next(resDrawable: Int) {
                binding.ivAddIv.visibility = View.GONE
                binding.ivCollection.visibility = View.VISIBLE
                binding.ivCollection.setImageResource(resDrawable)
                collectionData.image = resDrawable
            }

        }
    }

    private fun initTaskCollectionAdapter() {
        taskCollectionAdapter = BaseBindingAdapter<Task>(R.layout.item_task_collection,layoutInflater,
            object: DiffUtil.ItemCallback<Task>(){
                override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                    return  oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                    return oldItem == newItem
                }
            }
        )
        taskCollectionAdapter?.setListener(object : ItemBaseWithPostitionListener<Task>{
            override fun onItemClicked(item: Task, p: Int) {

            }

        })
        taskCollectionAdapter?.submitList(newTasks)
        binding.rcvTask.adapter = taskCollectionAdapter
    }

    private fun setEmptyCollection() {
        binding.layoutCreate.btnSave.text = "CREATE COLLECTION"
        binding.layoutCreate.vRoot.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.gray))
        binding.isShowListTask = false
    }

    override fun setEvent() {

        binding.ivAddIv.setOnClickListener {
            bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
        }

        binding.llAddTask.setOnClickListener {
            (requireActivity() as NewHabitActivity).let {
                it.newHabitFragment.newHabitFragmentState = NewHabitFragment.NewHabitFragmentState.ADDTOCOLLECTION
                it.addFragmentNotHide(it.newHabitFragment,NewHabitFragment.TAG)
            }
        }

        binding.layoutHeader.ivBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.layoutCreate.btnSave.setOnClickListener {
            if (hadChangeState && !binding.edtCollection.text.isNullOrEmpty()) {
                collectionData.name = binding.edtCollection.text.toString()
                collectionData.task = newTasks
                collectionData.colorBg = Helper.colorTask.random()

                Toast.makeText(requireContext(), "handle add collection to db", Toast.LENGTH_SHORT).show()
                lifecycleScope.launch {
                    collectionViewModel.userIntent.send(CollectionIntent.CreateCollection(collectionData))
                }
            }
        }
    }

    fun addTask(task: Task) {
        if (!hadChangeState) {
            binding.layoutCreate.vRoot.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.teal_green))
            binding.rcvTask.visibility = View.VISIBLE
            binding.llAddTask.visibility = View.GONE

            hadChangeState = true
        }



        newTasks.add(task)
        taskCollectionAdapter?.notifyItemInserted(newTasks.size-1)


    }


}