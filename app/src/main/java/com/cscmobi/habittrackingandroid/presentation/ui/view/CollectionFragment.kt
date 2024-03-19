package com.cscmobi.habittrackingandroid.presentation.ui.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewAnimationUtils
import android.widget.Filter
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.base.BaseBindingAdapter2
import com.cscmobi.habittrackingandroid.base.BaseFragment
import com.cscmobi.habittrackingandroid.data.model.HabitCollection
import com.cscmobi.habittrackingandroid.databinding.FragmentCollectionBinding
import com.cscmobi.habittrackingandroid.presentation.ItemDetailCollection
import com.cscmobi.habittrackingandroid.presentation.OnItemClickPositionListener
import com.cscmobi.habittrackingandroid.presentation.ui.activity.NewHabitActivity
import com.cscmobi.habittrackingandroid.presentation.ui.adapter.CollectionAdapter
import com.cscmobi.habittrackingandroid.presentation.ui.intent.CollectionIntent
import com.cscmobi.habittrackingandroid.presentation.ui.viewmodel.CollectionViewModel
import com.cscmobi.habittrackingandroid.presentation.ui.viewstate.CollectionState
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import com.cscmobi.habittrackingandroid.utils.Utils
import com.thanhlv.ads.lib.AdMobUtils
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale
import kotlin.math.hypot


class CollectionFragment :
    BaseFragment<FragmentCollectionBinding>(FragmentCollectionBinding::inflate) {

    private val collectionViewModel by activityViewModels<CollectionViewModel>()
    private var collectionAdapter: CollectionAdapter? = null
    private var detailCollectionAdapter: BaseBindingAdapter2<Task>? = null
    var listTasks = mutableListOf<Task>()
    var listCollections = mutableListOf<HabitCollection>()
    var isCollectionSelect = true

    override fun initView(view: View) {

//        binding.layoutHeader.ivSearch.visibility = View.VISIBLE
        initTasksAdapter()

        lifecycleScope.launch {
            collectionViewModel.userIntent.send(CollectionIntent.FetchCollection)

            collectionViewModel.state.collect {
                when (it) {
                    is CollectionState.Collections -> {
                        listCollections.clear()
                        listCollections = it.collection.toMutableList()
                        listTasks.clear()
                        listCollections.forEach {
                            if (!it.isEdit)
                                it.task?.let { tasks ->
                                    listTasks.addAll(tasks)
                                }
                        }
                        listCollections.add(0, HabitCollection())
                        initCollectionAdapter()
                    }

                    else -> {}
                }
            }
        }
        setUpSearchView()

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

    private fun setUpSearchView() {

//        binding.searchView.clearFocus()
//        binding.searchView.setOnQueryTextFocusChangeListener(OnFocusChangeListener { view, hasFocus ->
//            if (hasFocus) {
//                binding.searchView.setIconified(false);
////                binding.ivClose.visibility =View.VISIBLE
//
//            }
//        })
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                binding.ivClose.visibility = View.INVISIBLE

                if (isCollectionSelect) {
                    collectionAdapter?.filter?.filter(newText)
                } else detailCollectionAdapter?.filter?.filter(newText)

                if (newText.isNullOrEmpty()) {
                    binding.searchView.clearFocus()
                    binding.searchView.visibility = View.GONE
                    binding.layoutHeader.root.visibility = View.VISIBLE
                    binding.txtNewHabit.visibility = View.VISIBLE
                    binding.llCreateTask.visibility = View.VISIBLE
                }
                return true
            }
        })
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun initCollectionAdapter() {
//

        collectionAdapter = CollectionAdapter(
            requireContext(),
            listCollections,
            object : OnItemClickPositionListener {
                override fun onItemClick(position: Int) {
                    if (position == 0) {
                        lifecycleScope.launch {
                            collectionViewModel.userIntent.send(CollectionIntent.NotCreateCollection)
                        }

                        (requireActivity() as NewHabitActivity).let {
                            it.replaceFragment(
                                it.createCollectionFragment,
                                CreateCollectionFragment.TAG
                            )
                        }

                    } else {
                        lifecycleScope.launch {
                            collectionViewModel.userIntent.send(
                                CollectionIntent.PassItemCollection(
                                    collectionAdapter!!.currentList[position]
                                )
                            )
                        }
                        (requireActivity() as NewHabitActivity).replaceFragment(
                            DetailCollectionFragment(),
                            "Test"
                        )
                    }
                }

            })

        binding.rcvCollection.adapter = collectionAdapter
        collectionAdapter?.submitList(listCollections)
        collectionAdapter?.notifyDataSetChanged()
    }

    private fun initTasksAdapter() {
        layoutInflater.context
        detailCollectionAdapter = BaseBindingAdapter2<Task>(
            R.layout.item_detail_collection,
            layoutInflater,
            object : DiffUtil.ItemCallback<Task>() {
                override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                    return oldItem.name == newItem.name
                }

                override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                    return oldItem == newItem
                }
            }, originalData = listTasks
        )

        detailCollectionAdapter?.setFilter(
            object : Filter() {
                override fun performFiltering(constraint: CharSequence?): FilterResults {
                    val filteredList = if (constraint.isNullOrBlank()) {
                        listTasks
                    } else {
                        val filterPattern = constraint.toString().lowercase(Locale.getDefault())
                        listTasks.filter {
                            // Implement your filtering logic here
                            // For example, check if the item contains the constraint in its toString()
                            it.name.toString().lowercase(Locale.getDefault())
                                .contains(filterPattern)
                        }

                    }.distinctBy { it.name }


                    val results = FilterResults()
                    results.values = filteredList
                    return results
                }

                @Suppress("UNCHECKED_CAST")
                override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                    detailCollectionAdapter?.submitList(results?.values as? List<Task>)
                    detailCollectionAdapter?.notifyDataSetChanged()
                }
            }
        )
        detailCollectionAdapter?.setListener(object : ItemDetailCollection<Task> {
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
                                item
                            )
                        )

                    }
                }
            }

            override fun onAddTask(item: Task) {
                lifecycleScope.launch {
                    var clone = item.copy(startDate = Calendar.getInstance().time.time)
                    collectionViewModel.userIntent.send(CollectionIntent.CreateTaskToRoutine(clone))
                    Toast.makeText(requireContext(), "Create task success", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        })

        detailCollectionAdapter?.submitList(listTasks)
        detailCollectionAdapter?.notifyDataSetChanged()
        binding.rcvTasks.adapter = detailCollectionAdapter
    }


    override fun setEvent() {
        binding.btnCollection.setOnClickListener {

            changeStateCollectionButton(true)

            collectionAdapter?.filter?.filter(binding.searchView.query)
        }

        binding.btnAll.setOnClickListener {
            changeStateCollectionButton(false)
            detailCollectionAdapter?.filter?.filter(binding.searchView.query)

        }

        binding.llCreateTask.setOnClickListener {
            (requireActivity() as NewHabitActivity).let {
                it.newHabitFragment.newHabitFragmentState =
                    NewHabitFragment.NewHabitFragmentState.NEWTASK
                it.replaceFragment(it.newHabitFragment, NewHabitFragment.TAG)
            }
        }

        binding.layoutHeader.ivBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.layoutHeader.ivSearch.setOnClickListener {
            toggleSearchView()
        }

        val closeButton: View? =
            binding.searchView.findViewById(androidx.appcompat.R.id.search_close_btn)
        closeButton?.setOnClickListener {
            toggleSearchView()
        }

        binding.ivClose.setOnClickListener {
            toggleSearchView()
        }


    }


    private fun toggleSearchView() {
        if (binding.searchView.visibility == View.VISIBLE) {
            // If SearchView is visible, hide it
            hideSearchView()
            binding.layoutHeader.root.visibility = View.VISIBLE

        } else {
            // If SearchView is not visible, show it
            showSearchView()
            binding.layoutHeader.root.visibility = View.INVISIBLE
        }
    }

    private fun showSearchView() {
        binding.txtNewHabit.visibility = View.GONE
        binding.llCreateTask.visibility = View.GONE
        binding.ivClose.visibility = View.VISIBLE

        val endRadius = hypot(
            binding.searchView.width.toDouble(),
            binding.searchView.height.toDouble()
        ).toFloat()

        val anim = ViewAnimationUtils.createCircularReveal(
            binding.searchView,
            binding.searchView.width,
            binding.searchView.height,
            0f,
            endRadius
        )

        anim.duration = 500
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                binding.searchView.visibility = View.VISIBLE
                binding.searchView.requestFocus()
                showKeyboardOnView(binding.searchView)

            }
        })

        anim.start()
    }

    private fun hideSearchView() {
        binding.searchView.setQuery("", false)
        binding.txtNewHabit.visibility = View.VISIBLE
        binding.llCreateTask.visibility = View.VISIBLE
        binding.ivClose.visibility = View.GONE

        val startRadius =
            Math.hypot(binding.searchView.width.toDouble(), binding.searchView.height.toDouble())
                .toFloat()

        val anim = ViewAnimationUtils.createCircularReveal(
            binding.searchView,
            binding.searchView.width,
            binding.searchView.height,
            startRadius,
            0f
        )

        anim.duration = 500
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                binding.searchView.visibility = View.INVISIBLE
            }
        })

        anim.start()
    }

    private fun changeStateCollectionButton(isChanged: Boolean) {
        isCollectionSelect = isChanged
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
            binding.btnCollection.typeface =
                ResourcesCompat.getFont(context!!, R.font.worksans_semibold)
            binding.btnAll.typeface =
                ResourcesCompat.getFont(context!!, R.font.worksans_medium)

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
            binding.btnAll.typeface =
                ResourcesCompat.getFont(context!!, R.font.worksans_semibold)
            binding.btnCollection.typeface =
                ResourcesCompat.getFont(context!!, R.font.worksans_medium)

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

    override fun onDestroy() {
        super.onDestroy()
        listCollections = mutableListOf()
    }
}