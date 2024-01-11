package com.cscmobi.habittrackingandroid.presentation.ui.view

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.base.BaseFragment
import com.cscmobi.habittrackingandroid.databinding.FragmentCollectionBinding
import com.cscmobi.habittrackingandroid.presentation.OnItemClickPositionListener
import com.cscmobi.habittrackingandroid.presentation.ui.activity.NewHabitActivity
import com.cscmobi.habittrackingandroid.presentation.ui.adapter.CollectionAdapter
import com.cscmobi.habittrackingandroid.presentation.ui.intent.CollectionIntent
import com.cscmobi.habittrackingandroid.presentation.ui.viewmodel.CollectionViewModel
import com.cscmobi.habittrackingandroid.presentation.ui.viewstate.CollectionState
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CollectionFragment :
    BaseFragment<FragmentCollectionBinding>(FragmentCollectionBinding::inflate) {
    private val collectionViewModel: CollectionViewModel by viewModel()
    private lateinit var collectionAdapter: CollectionAdapter
    override fun initView(view: View) {

        binding.layoutHeader.ivSearch.visibility = View.VISIBLE
        collectionAdapter = CollectionAdapter(object : OnItemClickPositionListener {
            override fun onItemClick(position: Int) {
                if(position == 0 ){

                } else {
                    (requireActivity() as NewHabitActivity).replaceFragment(DetailCollectionFragment(),"Test")
                }
            }

        })

        binding.rcvCollection.adapter = collectionAdapter

        lifecycleScope.launch {
            collectionViewModel.userIntent.send(CollectionIntent.FetchCollection)

            collectionViewModel.state.collect {
                when (it) {
                    is CollectionState.Collections -> {
                        collectionAdapter.submitList(it.collection)
                        collectionAdapter.notifyDataSetChanged()
                    }

                    else -> {}
                }
            }
        }

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
    }

    private fun changeStateCollectionButton(isChanged: Boolean) {
        if (isChanged) {
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
            binding.btnCollection.typeface = ResourcesCompat.getFont(context!!, R.font.montserratalternates_extrabold)
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
            binding.btnAll.typeface = ResourcesCompat.getFont(context!!, R.font.montserratalternates_extrabold)
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