package com.cscmobi.habittrackingandroid.presentation.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DiffUtil
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.base.BaseBindingAdapter
import com.cscmobi.habittrackingandroid.data.model.Tag
import com.cscmobi.habittrackingandroid.databinding.BottomsheetFragmentNewHabitBinding
import com.cscmobi.habittrackingandroid.presentation.ItemBasePosistionListener
import com.cscmobi.habittrackingandroid.presentation.ui.viewmodel.CollectionViewModel
import com.cscmobi.habittrackingandroid.thanhlv.database.AppDatabase
import com.cscmobi.habittrackingandroid.thanhlv.model.Tags
import com.cscmobi.habittrackingandroid.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.runBlocking


class BottomsheetNewHabitFragment :
    BottomSheetDialogFragment() {

    private val collectionViewModel by activityViewModels<CollectionViewModel>()

    lateinit var binding: BottomsheetFragmentNewHabitBinding
    var caseType = 0
    var tagSelect = ""
    var unitSelect = ""
    var taskAdapter: BaseBindingAdapter<Tag>? = null
    var oldPostionTag = 0
    private var tags = mutableListOf<Tag>()

    var bottomlistener: BottomListener? = null

    fun setListener(listener: BottomListener) {
        bottomlistener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomsheetFragmentNewHabitBinding.inflate(inflater, container, false)
        binding.caseType = caseType
        if (caseType == 1) {
            binding.txtTitle.text = getString(R.string.enter_how_often_you_want_the_task_to_repeat)
            binding.txtUnit.text = unitSelect.ifEmpty { "Week" }
            binding.edt.inputType = EditorInfo.TYPE_CLASS_NUMBER

        } else
            initTag()

        binding.ivBack.setOnClickListener {
            this.dismiss()
        }

        binding.llCreateTag.setOnClickListener {
            binding.caseType = 1
            binding.txtTitle.text = getString(R.string.enter_name_tag)
            binding.layoutBtnSave.btnSave.text = getString(R.string.create)
            binding.txtUnit.visibility = View.GONE

        }

        binding.layoutBtnSave.btnSave.setOnClickListener {
            if (caseType == 1) {
                // input repeat
                if (!invalidateRepeat()) return@setOnClickListener
                bottomlistener?.saveUnitNumberRepeat(binding.edt.text.toString().toInt())
                this.dismiss()
            } else {
                // input tag
                val newTag = binding.edt.text.toString()
                if (newTag.isEmpty() && newTag != "No Tag") {
                    bottomlistener?.createTag(newTag)
                    runBlocking {
                        AppDatabase.getInstance(requireContext()).dao()
                            .insertTag(Tags(newTag))
                    }
                }
                this.dismiss()
            }
        }
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initTag() {
        taskAdapter = BaseBindingAdapter(
            R.layout.item_tag,
            layoutInflater,
            object : DiffUtil.ItemCallback<Tag>() {
                override fun areItemsTheSame(oldItem: Tag, newItem: Tag): Boolean {
                    return oldItem.name == newItem.name
                }

                override fun areContentsTheSame(oldItem: Tag, newItem: Tag): Boolean {
                    return oldItem == newItem
                }
            })
        taskAdapter?.submitList(tags)
        taskAdapter?.setListener(object : ItemBasePosistionListener {
            override fun onItemClicked(p: Int) {
                if (oldPostionTag != -1 && oldPostionTag != p) {
                    tags[oldPostionTag].isSelected = false
                    oldPostionTag = p

                } else {
                    oldPostionTag = p
                }

                tags[p].isSelected = true

                bottomlistener?.createTag(tags[p].name)
                taskAdapter?.notifyDataSetChanged()

            }
        })

        binding.adapter = taskAdapter

        runBlocking {
            tags = collectionViewModel.tag()
            if (tagSelect.isEmpty()) {
                tags[0].isSelected = true
            } else {
                val pos = tags.indexOfFirst { it.name == tagSelect }
                if (pos != -1) tags[pos].isSelected = true
            }
            taskAdapter?.submitList(tags)
            taskAdapter?.notifyDataSetChanged()
        }
    }

    private fun invalidateRepeat(): Boolean {
        var rangeMaxUnit = 1
        when (unitSelect) {
            "Day" -> rangeMaxUnit = 99
            "Week" -> rangeMaxUnit = 52
            "Month" -> rangeMaxUnit = 12
        }

        if (binding.edt.text.toString().toInt() > rangeMaxUnit) {
            Toast.makeText(requireContext(), "range in 1 to $rangeMaxUnit", Toast.LENGTH_SHORT)
                .show()
            return false
        }
        return true
    }

    interface BottomListener {
        fun saveUnitNumberRepeat(number: Int)
        fun createTag(name: String)
    }

}