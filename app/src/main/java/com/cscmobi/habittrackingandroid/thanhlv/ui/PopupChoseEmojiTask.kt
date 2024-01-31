package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.app.Dialog
import android.content.res.AssetManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.databinding.PopupChoseAvaProfileLayoutBinding
import com.cscmobi.habittrackingandroid.databinding.PopupChoseEmojiTaskBinding
import com.cscmobi.habittrackingandroid.thanhlv.adapter.EmojiAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.thanhlv.fw.spf.SPF

class PopupChoseEmojiTask : BottomSheetDialogFragment() {
    var callback: Callback? = null

    private lateinit var binding: PopupChoseEmojiTaskBinding
    private var adapter: EmojiAdapter ? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PopupChoseEmojiTaskBinding.inflate(inflater, container, false)
        mListEmoji = getPathAsset()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val offsetFromTop = 200
        (dialog as? BottomSheetDialog)?.behavior?.apply {
            isFitToContents = true
//            expandedOffset = offsetFromTop
            state = BottomSheetBehavior.STATE_EXPANDED
        }

        initView()
        controllerView()

    }

    private fun initView() {

        if (adapter == null) {
            adapter = EmojiAdapter(requireContext(), mListEmoji)
            adapter!!.setCallback(object : EmojiAdapter.EmojiCallback {
                override fun onClickItem(emoji: String) {
                    callback?.clickChange(emoji)
                    dismissAllowingStateLoss()
                }
            })
            binding.rcEmoji.adapter = adapter
            binding.rcEmoji.layoutManager = GridLayoutManager(requireContext(), 5)
        }

    }

    private var mListEmoji = listOf<String>()
    private fun getPathAsset(): List<String> {

        val assetManager: AssetManager = requireContext().assets
        val imageFolder = "avatask"
        val imageFiles: Array<String> = assetManager.list(imageFolder) ?: emptyArray()
        return imageFiles.map { "$imageFolder/$it"}
    }

    private fun controllerView() {

        binding.btnBackHeader.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnShowListener {
                val bottomSheet =
                    findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                bottomSheet.setBackgroundResource(android.R.color.transparent)
            }
        }
    }

    interface Callback {
        fun clickChange(ava: String)
    }

}