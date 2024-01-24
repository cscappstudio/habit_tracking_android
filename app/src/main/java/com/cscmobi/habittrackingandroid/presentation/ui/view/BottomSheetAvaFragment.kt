package com.cscmobi.habittrackingandroid.presentation.ui.view

import android.app.Dialog
import android.content.res.AssetManager
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.base.BaseBindingAdapter
import com.cscmobi.habittrackingandroid.databinding.BottomSheetIconTaskBinding
import com.cscmobi.habittrackingandroid.presentation.ItemBaseListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetAvaFragment : BottomSheetDialogFragment() {
    var icons =  mutableListOf<String>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = BottomSheetIconTaskBinding.inflate(inflater, container, false)
        val adapter = BaseBindingAdapter<String>(R.layout.item_ava_task,layoutInflater,object : DiffUtil.ItemCallback<String>(){
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

        })
        adapter.setListener(object : ItemBaseListener<String>{
            override fun onItemClicked(item: String) {

            }

        })
        icons = getPathAsset().toMutableList()
        adapter.submitList(icons)

        val spacingInPixels = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._20sdp) // Adjust the spacing dimension
        binding.rcvIcon.addItemDecoration(GridSpacingItemDecoration(5, spacingInPixels, true))
        binding.rcvIcon.adapter =  adapter

        return   binding.root
    }

    private fun getPathAsset(): List<String> {

        val assetManager: AssetManager = requireContext().assets
        val imageFolder = "avatask"
        val imageFiles: Array<String> = assetManager.list(imageFolder) ?: emptyArray()
        return imageFiles.map { "$imageFolder/$it"}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val offsetFromTop = 200
        (dialog as? BottomSheetDialog)?.behavior?.apply {
            isFitToContents = false
            setExpandedOffset(offsetFromTop)
            state = BottomSheetBehavior.STATE_EXPANDED
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnShowListener {
                val bottomSheet = findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                bottomSheet.setBackgroundResource(android.R.color.transparent)
            }
        }


    }
    private class GridSpacingItemDecoration(
        private val spanCount: Int,
        private val spacing: Int,
        private val includeEdge: Boolean
    ) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect, view: View,
            parent: RecyclerView, state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view) // item position
            val column = position % spanCount // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount
                outRect.right = (column + 1) * spacing / spanCount

                if (position < spanCount) {
                    outRect.top = spacing
                }
                outRect.bottom = spacing
            } else {
                outRect.left = column * spacing / spanCount
                outRect.right = spacing - (column + 1) * spacing / spanCount
                if (position >= spanCount) {
                    outRect.top = spacing
                }
            }
        }
    }
    interface aa {
        fun next(resDrawable: Int)
    }
}