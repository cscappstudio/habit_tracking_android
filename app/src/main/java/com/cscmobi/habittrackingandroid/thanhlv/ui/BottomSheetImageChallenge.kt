package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.base.BaseBindingAdapter
import com.cscmobi.habittrackingandroid.data.model.AlbumCollection
import com.cscmobi.habittrackingandroid.databinding.BottomSheetAlbumCollectionFragmentBinding
import com.cscmobi.habittrackingandroid.presentation.ItemBasePosistionListener
import com.cscmobi.habittrackingandroid.thanhlv.adapter.EmojiAdapter
import com.cscmobi.habittrackingandroid.thanhlv.adapter.ImageChallengeAdapter
import com.cscmobi.habittrackingandroid.thanhlv.model.ImageChallengeModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetImageChallenge :
    BottomSheetDialogFragment() {

    var albums = mutableListOf<ImageChallengeModel>(
        ImageChallengeModel("album_collection1.png"),
        ImageChallengeModel("album_collection2.png"),
        ImageChallengeModel("album_collection3.png"),
        ImageChallengeModel("album_collection4.png"),
        ImageChallengeModel("album_collection5.png"),
        ImageChallengeModel("album_collection6.png"),
        ImageChallengeModel("album_collection7.png"),
        ImageChallengeModel("album_collection8.png"),
        ImageChallengeModel("album_collection9.png"),
        ImageChallengeModel("album_collection10.png"),
        ImageChallengeModel("album_collection11.png"),
        ImageChallengeModel("album_collection12.png"),
        ImageChallengeModel("album_collection13.png"),
        ImageChallengeModel("album_collection14.png"),
        ImageChallengeModel("album_collection15.png"),
        ImageChallengeModel("album_collection16.png"),
        ImageChallengeModel("album_collection17.png"),
        ImageChallengeModel("album_collection18.png"),
        ImageChallengeModel("album_collection19.png"),
        ImageChallengeModel("album_collection20.png")
    )

    var listener: IBottomCollection? = null
    var resDrawablesSelect = "album_collection1.png"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = BottomSheetAlbumCollectionFragmentBinding.inflate(inflater, container, false)
        binding.layoutNext.btnSave.text = getString(R.string.next)

        binding.edtCollection.text = getString(R.string.choose_your_challenge_cover_image)

        val adapter = ImageChallengeAdapter(requireContext(), albums)
        adapter.setCallback(object : ImageChallengeAdapter.ImageChallengeCallback {
            @SuppressLint("NotifyDataSetChanged")
            override fun onClickItem(image: String) {

                adapter.notifyDataSetChanged()

                resDrawablesSelect = image

                binding.layoutNext.btnSave.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.teal_green
                    )
                )

                if (binding.ivSelect.visibility == View.GONE) {
                    binding.ivAddIv.visibility = View.GONE
                    binding.ivSelect.visibility = View.VISIBLE
                }

                binding.ivSelect.setImageBitmap(
                    BitmapFactory.decodeStream(
                        requireContext().assets.open(
                            resDrawablesSelect
                        )
                    )
                )

            }
        })
        binding.rcvAlbum.adapter = adapter
        binding.rcvAlbum.layoutManager = GridLayoutManager(requireContext(), 2)


        binding.layoutNext.btnSave.setOnClickListener {
            listener?.next(resDrawablesSelect)
            dismissAllowingStateLoss()

        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val offsetFromTop = 200
        (dialog as? BottomSheetDialog)?.behavior?.apply {
            isFitToContents = false
            expandedOffset = offsetFromTop
            state = BottomSheetBehavior.STATE_EXPANDED
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

    interface IBottomCollection {
        fun next(resDrawable: String)
    }
}