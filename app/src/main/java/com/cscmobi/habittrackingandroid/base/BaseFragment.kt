package com.cscmobi.habittrackingandroid.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.cscmobi.habittrackingandroid.utils.Utils.resetResouceConfig


typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB: ViewBinding>(
    private val inflate: Inflate<VB>,
) : Fragment() {
    private lateinit var _binding: VB
    val binding get() = _binding



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)
        setEvent()
    }

    abstract fun initView(view: View)
    abstract fun setEvent()

    fun showKeyboardOnView(view: View){
        view.post {
            view.requestFocus()
            val imgr = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imgr.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    open fun hideSoftKeyboard(mFragment: Fragment?) {
        try {
            if (mFragment == null || mFragment.activity == null) {
                return
            }
            val view: View? = mFragment.requireActivity().currentFocus
            if (view != null) {
                val inputManager: InputMethodManager =
                    mFragment.requireActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}