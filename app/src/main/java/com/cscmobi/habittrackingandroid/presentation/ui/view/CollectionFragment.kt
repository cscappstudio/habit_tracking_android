package com.cscmobi.habittrackingandroid.presentation.ui.view

import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.base.BaseBindingAdapter
import com.cscmobi.habittrackingandroid.base.BaseFragment
import com.cscmobi.habittrackingandroid.databinding.FragmentCollectionBinding
import com.cscmobi.habittrackingandroid.presentation.ItemBaseListener
import com.cscmobi.habittrackingandroid.presentation.OnItemClickPositionListener
import com.cscmobi.habittrackingandroid.presentation.ui.activity.NewHabitActivity
import com.cscmobi.habittrackingandroid.presentation.ui.adapter.CollectionAdapter
import com.cscmobi.habittrackingandroid.presentation.ui.intent.CollectionIntent
import com.cscmobi.habittrackingandroid.presentation.ui.viewmodel.CollectionViewModel
import com.cscmobi.habittrackingandroid.presentation.ui.viewstate.CollectionState
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CollectionFragment :
    BaseFragment<FragmentCollectionBinding>(FragmentCollectionBinding::inflate) {

    //    private val collectionViewModel: CollectionViewModel by viewModel()
   private val collectionViewModel  by activityViewModels<CollectionViewModel>()
    private lateinit var collectionAdapter: CollectionAdapter
    lateinit var detailCollectionAdapter: BaseBindingAdapter<Task>
    var listTasks = mutableListOf<Task>()

    override fun initView(view: View) {


        binding.layoutHeader.ivSearch.visibility = View.VISIBLE
        collectionAdapter = CollectionAdapter(object : OnItemClickPositionListener {
            override fun onItemClick(position: Int) {
                if(position == 0 ){
                    (requireActivity() as NewHabitActivity).let {
                        it.replaceFragment(it.createCollectionFragment,CreateCollectionFragment.TAG)
                    }

                } else {
                    lifecycleScope.launch {
                        collectionViewModel.userIntent.send(CollectionIntent.PassItemCollection(collectionAdapter.currentList[position]))
                    }
                    (requireActivity() as NewHabitActivity).replaceFragment(DetailCollectionFragment(),"Test")
                }
            }

        })



        binding.rcvCollection.adapter = collectionAdapter

        //
        initTasksAdapter()


        lifecycleScope.launch {
            collectionViewModel.userIntent.send(CollectionIntent.FetchCollection)

            collectionViewModel.state.collect {
                when (it) {
                    is CollectionState.Collections -> {
                        collectionAdapter.submitList(it.collection)
                        collectionAdapter.notifyDataSetChanged()

                        listTasks.clear()
                      it.collection.forEach {
                          it.task?.let { tasks ->
                              listTasks.addAll(tasks)
                          }
                      }
                        detailCollectionAdapter.submitList(listTasks)
                        detailCollectionAdapter.notifyDataSetChanged()
                    }

                    else -> {}
                }
            }
        }

    }


    private fun initTasksAdapter() {
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
        detailCollectionAdapter.setListener(object: ItemBaseListener<Task> {
            override fun onItemClicked(item: Task) {
                (requireActivity() as NewHabitActivity).let {
                    it.newHabitFragment.newHabitFragmentState = NewHabitFragment.NewHabitFragmentState.ADDTOROUTINE
                    it.addFragmentNotHide(it.newHabitFragment,NewHabitFragment.TAG)
                    lifecycleScope.launch {
                        collectionViewModel.userIntent.send(CollectionIntent.PassTaskfromCollection(item))

                    }
                }
            }

        })
        binding.rcvTasks.adapter = detailCollectionAdapter


    }


    override fun setEvent() {
        binding.btnCollection.setOnClickListener {
            lifecycleScope.launch {
                collectionViewModel.userIntent.send(CollectionIntent.FetchCollection)
            }
            changeStateCollectionButton(true)
        }

        binding.btnAll.setOnClickListener {
            changeStateCollectionButton(false)
        }

        binding.llCreateTask.setOnClickListener {
            (requireActivity() as NewHabitActivity).let {
                it.newHabitFragment.newHabitFragmentState = NewHabitFragment.NewHabitFragmentState.ADDTOROUTINE
                it.replaceFragment(it.newHabitFragment,NewHabitFragment.TAG)
            }
        }

        binding.layoutHeader.ivBack.setOnClickListener {
            requireActivity().finish()
        }
    }

    private fun changeStateCollectionButton(isChanged: Boolean) {
        if (isChanged) {
            binding.rcvCollection.visibility = View.VISIBLE
            binding.rcvTasks.visibility = View.GONE

            binding.btnCollection.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            binding.btnCollection.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(), R.color.tangerine
                )
            )

            binding.btnCollection.elevation = 20f
            binding.btnAll.elevation = 0f
            binding.btnCollection.typeface = ResourcesCompat.getFont(context!!, R.font.montserratalternates_semibold)
            binding.btnAll.typeface = ResourcesCompat.getFont(context!!, R.font.montserratalternates_medium)

            binding.btnAll.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.gray
                )
            )
            binding.btnAll.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(), R.color.white_smoke
                )
            )


        } else {
            binding.rcvTasks.visibility = View.VISIBLE
            binding.rcvCollection.visibility = View.GONE

            binding.btnAll.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            binding.btnAll.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(), R.color.tangerine
                )
            )

            binding.btnAll.elevation = 20f
            binding.btnCollection.elevation = 0f
            binding.btnAll.typeface = ResourcesCompat.getFont(context!!, R.font.montserratalternates_semibold)
            binding.btnCollection.typeface = ResourcesCompat.getFont(context!!, R.font.montserratalternates_medium)

            binding.btnCollection.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.gray
                )
            )
            binding.btnCollection.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(), R.color.white_smoke
                )
            )
        }
    }
}