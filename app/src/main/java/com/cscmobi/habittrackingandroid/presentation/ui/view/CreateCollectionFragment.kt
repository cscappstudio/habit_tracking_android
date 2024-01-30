package com.cscmobi.habittrackingandroid.presentation.ui.view

import android.content.res.ColorStateList
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.FragmentManager
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
import com.cscmobi.habittrackingandroid.presentation.ItemTaskCollection
import com.cscmobi.habittrackingandroid.presentation.ui.activity.NewHabitActivity
import com.cscmobi.habittrackingandroid.presentation.ui.intent.CollectionIntent
import com.cscmobi.habittrackingandroid.presentation.ui.viewmodel.CollectionViewModel
import com.cscmobi.habittrackingandroid.presentation.ui.viewstate.CollectionState
import com.cscmobi.habittrackingandroid.utils.CustomEditMenu
import com.cscmobi.habittrackingandroid.utils.Helper
import com.cscmobi.habittrackingandroid.utils.setDrawableString
import kotlinx.coroutines.launch

class CreateCollectionFragment :
    BaseFragment<FragmentCreateCollectionBinding>(FragmentCreateCollectionBinding::inflate) {

    private var hadChangeState = false
    private val collectionViewModel by activityViewModels<CollectionViewModel>()
    private val bottomSheetFragment = BottomSheetCollectionFragment()
    private var newTasks = mutableListOf<Task>()
    private var taskCollectionAdapter: BaseBindingAdapter<Task>? = null
    private var currentPos = 0
    var collectionData: HabitCollection = HabitCollection()
    var isEdit = false

    companion object {
        val TAG = "CreateCollectionFragment"
    }

    override fun initView(view: View) {

        hadChangeState = false
        newTasks.clear()

        initTaskCollectionAdapter()




        bottomSheetFragment.listener = object : BottomSheetCollectionFragment.IBottomCollection {
            override fun next(resDrawable: Int) {
                binding.ivAddIv.visibility = View.GONE
                binding.ivCollection.visibility = View.VISIBLE
                binding.ivCollection.setImageResource(resDrawable)
                collectionData.image = requireContext().resources.getResourceEntryName(resDrawable)
            }

        }
    }

    private fun setUpCollection(data: HabitCollection) {
        isEdit = true
        collectionData = data
//        binding.isShowListTask = true

        binding.ivAddIv.visibility = View.GONE
        binding.ivCollection.visibility = View.VISIBLE
        data.image?.let { binding.ivCollection.setDrawableString(it) }

        binding.edtCollection.setText(data.name)
        data.task?.let { newTasks.addAll(it) }
        taskCollectionAdapter?.notifyDataSetChanged()


        binding.layoutCreate.btnSave.text = "SAVE"

    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            collectionViewModel.state.collect {
                if (it is CollectionState.IdleCreateCollection) {
                    setEmptyCollection()


                } else if (it is CollectionState.UpdateCollection) {
                    setUpCollection(it.data)

                }

            }
        }
    }

    private fun initTaskCollectionAdapter() {
        taskCollectionAdapter =
            BaseBindingAdapter<Task>(R.layout.item_task_collection, layoutInflater,
                object : DiffUtil.ItemCallback<Task>() {
                    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                        return oldItem.name == newItem.name                   }

                    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                        return oldItem == newItem
                    }
                }
            )
        taskCollectionAdapter?.setListener(object : ItemTaskCollection<Task> {
            override fun onItemClicked(item: Task, p: Int) {
            }

            override fun onEdit(v: View, item: Task, p: Int) {
                currentPos = p
                setPopUpWindow(v,p,item)
            }

        })
        taskCollectionAdapter?.submitList(newTasks)
        binding.rcvTask.adapter = taskCollectionAdapter
    }

    private fun setEmptyCollection() {
        binding.layoutCreate.btnSave.text = "CREATE COLLECTION"
        binding.edtCollection.setText("")
        binding.layoutCreate.vRoot.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.gray))
        binding.isShowListTask = false
        isEdit = false
    }

    override fun setEvent() {

        binding.ivAddIv.setOnClickListener {
            bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
        }

        binding.llAddTask.setOnClickListener {
            (requireActivity() as NewHabitActivity).let {
                it.newHabitFragment.newHabitFragmentState =
                    NewHabitFragment.NewHabitFragmentState.ADDTOCOLLECTION
                it.addFragmentNotHide(it.newHabitFragment, NewHabitFragment.TAG)
            }
        }

        binding.layoutHeader.ivBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.layoutCreate.btnSave.setOnClickListener {
            if (!validateEditText()) {
                return@setOnClickListener
            }

            if (!isEdit) {
                if (hadChangeState && !binding.edtCollection.text.isNullOrEmpty()) {
                    collectionData.name = binding.edtCollection.text.toString()
                    collectionData.task = newTasks
                    collectionData.resColorBg = Helper.colorTask.random()

                    Toast.makeText(
                        requireContext(),
                        "create collection success",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    lifecycleScope.launch {
                        collectionViewModel.userIntent.send(
                            CollectionIntent.CreateCollection(
                                collectionData
                            )
                        )
                    }
                    parentFragmentManager.popBackStack()
                } else Toast.makeText(
                    requireContext(),
                    "Please input name collection or add task",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                lifecycleScope.launch {
                    collectionData.name = binding.edtCollection.text.toString()
                    newTasks.forEach {
                        it.startDate = null
                    }
                    collectionData.task = newTasks
                    collectionViewModel.userIntent.send(CollectionIntent.UpdateCollection(collectionData))
                    parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }

            }
       }
    }

    private fun validateEditText(): Boolean {
       if (binding.edtCollection.text.isNullOrEmpty())  {
           Toast.makeText(requireContext(), "Name is empty", Toast.LENGTH_SHORT).show()
           return false
       }

        if (collectionViewModel.isExistCollectionName(binding.edtCollection.text.toString())) {
            Toast.makeText(requireContext(), "Name is exist", Toast.LENGTH_SHORT).show()

            return false
        }

        return true
    }

    fun addTask(task: Task) {
        task.startDate = null
        if (!hadChangeState) {
            binding.layoutCreate.vRoot.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.teal_green))
            binding.rcvTask.visibility = View.VISIBLE

            hadChangeState = true
        }

        newTasks.add(task.copy())
        taskCollectionAdapter?.notifyItemInserted(newTasks.size - 1)

    }


    fun setPopUpWindow(v: View,p: Int,e: Task) {
        val popup = CustomEditMenu(requireContext(),{
            (requireActivity() as NewHabitActivity).let {
                it.addFragmentNotHide(it.newHabitFragment, NewHabitFragment.TAG)
                it.newHabitFragment.newHabitFragmentState = NewHabitFragment.NewHabitFragmentState.EDITTASKCOLLECTION
                collectionViewModel.taskCollection =newTasks[currentPos]

            }

        },{
            newTasks.remove(e)
            taskCollectionAdapter?.notifyItemRemoved(p)
        })
        popup.showAsDropDown(v)

    }

    fun editTask(task: Task) {
        newTasks[currentPos] = task
        taskCollectionAdapter?.notifyItemChanged(currentPos)

    }


}