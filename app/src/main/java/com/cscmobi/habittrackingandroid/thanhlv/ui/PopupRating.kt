package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.databinding.PopupRatingBinding
import com.thanhlv.fw.constant.AppConfigs.Companion.KEY_PASS_REVIEW_APP
import com.thanhlv.fw.helper.MyUtils.Companion.hideNavigationBar
import com.thanhlv.fw.remoteconfigs.RemoteConfigs


class PopupRating : PopupBase() {


    private lateinit var binding: PopupRatingBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PopupRatingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideNavigationBar(requireActivity())
        initView()
        handleClick()
    }

    override fun clickBackSystem() {
        dismissAllowingStateLoss()
        hideNavigationBar(requireActivity())
    }

    override fun updateUI() {
    }

    private var star = 5f
    private fun handleClick() {

        binding.starRating.setOnRatingBarChangeListener { _, value, _ ->
            star = value
            if (value == 5f) {
                binding.imgTop.setImageResource(R.drawable.img_rating_heart)
                binding.imgTop2.setImageResource(R.drawable.img_rating_heart)
            } else if (value >= 4f) {
                binding.imgTop.setImageResource(R.drawable.img_rating_happy)
                binding.imgTop2.setImageResource(R.drawable.img_rating_happy)
            } else {
                binding.imgTop.setImageResource(R.drawable.img_rating_sad)
                binding.imgTop2.setImageResource(R.drawable.img_rating_sad)
            }
            if (RemoteConfigs.instance.getConfigValue(KEY_PASS_REVIEW_APP)
                    .asBoolean()
            ) {
                if (value <= 4) binding.edtFeedback.visibility = View.VISIBLE
            }
        }

        binding.btnNegative.setOnClickListener {
            mCallback?.clickNegative()
            dismissAllowingStateLoss()
        }

        binding.btnPositive.setOnClickListener {
            binding.ratingView.visibility = View.GONE
            binding.showThanks.visibility = View.VISIBLE
            if (star > 4
                || !RemoteConfigs.instance.getConfigValue(KEY_PASS_REVIEW_APP)
                    .asBoolean()
            ) {
                binding.tvPleaseGoStore.visibility = View.VISIBLE
            }
            Handler(Looper.getMainLooper()).postDelayed({
                mCallback?.clickPositive(star)
                dismissAllowingStateLoss()
            }, 1000)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
    }

    interface ConfirmCallback {
        fun clickNegative()
        fun clickPositive(value: Float)
    }

    var mCallback: ConfirmCallback? = null

    fun setCallback(callback: ConfirmCallback) {
        mCallback = callback
    }

    override fun onStart() {
        super.onStart()
        try {
            if (dialog != null) {
                dialog!!.setCanceledOnTouchOutside(false)
            }
        } catch (e: Exception) {
            handleException(e)
        }
    }

    companion object {
        fun newInstance(): PopupRating {
            return PopupRating()
        }
    }
}
