package com.cscmobi.habittrackingandroid.presentation.ui.view

import android.app.Dialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.base.BaseBindingAdapter
import com.cscmobi.habittrackingandroid.data.model.AlbumCollection
import com.cscmobi.habittrackingandroid.databinding.BottomSheetAlbumCollectionFragmentBinding
import com.cscmobi.habittrackingandroid.presentation.ItemBasePosistionListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetCollectionFragment :
    BottomSheetDialogFragment() {
    var oldPostionSelect = 0

    var albums = mutableListOf<AlbumCollection>(
        AlbumCollection(R.drawable.album_collection1),
        AlbumCollection(R.drawable.album_collection2),
        AlbumCollection(R.drawable.album_collection3),
        AlbumCollection(R.drawable.album_collection4),
        AlbumCollection(R.drawable.album_collection5),
        AlbumCollection(R.drawable.album_collection6),
        AlbumCollection(R.drawable.album_collection7),
        AlbumCollection(R.drawable.album_collection8),
        AlbumCollection(R.drawable.album_collection9),
        AlbumCollection(R.drawable.album_collection10),
        AlbumCollection(R.drawable.album_collection11),
        AlbumCollection(R.drawable.album_collection12),
        AlbumCollection(R.drawable.album_collection13),
        AlbumCollection(R.drawable.album_collection14),
        AlbumCollection(R.drawable.album_collection15),
        AlbumCollection(R.drawable.album_collection16),
        AlbumCollection(R.drawable.album_collection17),
        AlbumCollection(R.drawable.album_collection18),
        AlbumCollection(R.drawable.album_collection19),
        AlbumCollection(R.drawable.album_collection20),
    )

    var listener: IBottomCollection? = null
    var resDrawablesSelect = -1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = BottomSheetAlbumCollectionFragmentBinding.inflate(inflater, container, false)
        binding.layoutNext.btnSave.text = getString(R.string.next)

        val albumAdapter = BaseBindingAdapter<AlbumCollection>(
            R.layout.item_album_collection,
            layoutInflater,
            object : DiffUtil.ItemCallback<AlbumCollection>() {
                override fun areItemsTheSame(
                    oldItem: AlbumCollection,
                    newItem: AlbumCollection
                ): Boolean {
                    return oldItem.resDrawable == newItem.resDrawable
                }

                override fun areContentsTheSame(
                    oldItem: AlbumCollection,
                    newItem: AlbumCollection
                ): Boolean {
                    return oldItem == newItem
                }
            })

        albumAdapter.setListener(object : ItemBasePosistionListener {
            override fun onItemClicked(p: Int) {
                if (oldPostionSelect != -1 && oldPostionSelect != p) {
                    albums[oldPostionSelect].isSelected = false
                    oldPostionSelect = p

                } else {
                    oldPostionSelect = p
                }

                albums[p].isSelected = true

                albumAdapter?.notifyDataSetChanged()

                resDrawablesSelect = albums[p].resDrawable

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

                binding.ivSelect.setImageResource(resDrawablesSelect)
            }

        })
        albumAdapter.submitList(albums)

        binding.rcvAlbum.adapter = albumAdapter

        binding.layoutNext.btnSave.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(
                requireContext(),
                R.color.gray
            )
        )

        binding.layoutNext.btnSave.setOnClickListener {
            if (resDrawablesSelect != -1) {
                listener?.next(resDrawablesSelect)
                this.dismiss()
            }
        }


        return binding.root
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
                val bottomSheet =
                    findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                bottomSheet.setBackgroundResource(android.R.color.transparent)
            }
        }


    }


    interface IBottomCollection {
        fun next(resDrawable: Int)
    }
}