package com.cscmobi.habittrackingandroid.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.databinding.DialogCongratulationBinding
import com.cscmobi.habittrackingandroid.databinding.DialogDeleteChallengeBinding
import com.cscmobi.habittrackingandroid.databinding.DialogDeleteTaskBinding
import com.cscmobi.habittrackingandroid.databinding.DialogWatchAdsBinding
import com.cscmobi.habittrackingandroid.thanhlv.ui.SubscriptionsActivity
import com.google.android.gms.ads.FullScreenContentCallback
import com.thanhlv.ads.lib.AdMobUtils
import com.thanhlv.fw.helper.DisplayUtils

object DialogUtils {
    fun showDeleteChallenge(context: Context, deleteFuture: () -> Unit, deleteAll: () -> Unit) {
        val binding = DialogDeleteChallengeBinding.inflate(LayoutInflater.from(context));
        val alertDialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .show()
        alertDialog.window?.setBackgroundDrawable(null)

        binding.ivClose.setOnClickListener {
            alertDialog.dismiss()
        }

        binding.btnDelete.setOnClickListener {
            deleteFuture.invoke()
            alertDialog.dismiss()
        }

        binding.btnCancel.setOnClickListener {
            deleteAll.invoke()
            alertDialog.dismiss()
        }
    }

    fun showDeleteTaskDialog(context: Context, deleteFuture: () -> Unit, deleteAll: () -> Unit) {
        val binding = DialogDeleteTaskBinding.inflate(LayoutInflater.from(context));
        val alertDialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .show()
        alertDialog.window?.setBackgroundDrawable(null)

        binding.ivClose.setOnClickListener {
            alertDialog.dismiss()
        }

        binding.btnDeleteFuture.setOnClickListener {
            deleteFuture.invoke()
            alertDialog.dismiss()
        }

        binding.btnDeleteAll.setOnClickListener {
            deleteAll.invoke()
            alertDialog.dismiss()
        }
    }

    fun showCongratulationDialog(
        context: Context,
        title: String,
        content1: SpannableString,
        content2: String
    ) {
        val binding = DialogCongratulationBinding.inflate(LayoutInflater.from(context))
        val alertDialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .show()
        alertDialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        alertDialog.window?.setBackgroundDrawable(null)

        val layoutParams = binding.ctlRoot.layoutParams
        layoutParams.width = DisplayUtils.getDeviceWidthPx().toInt()
        layoutParams.height = DisplayUtils.getDeviceHeightPx().toInt()
        binding.ctlRoot.layoutParams = layoutParams

        binding.txtTitle.text = title
        binding.txtContent1.text = content1
        binding.txtContent2.text = content2

        binding.btnClaim.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    fun showWatchAdsDialog(
        activity: Activity,
        watchAdsTimes: Int,
        title: String,
        des: String,
        watchAdsDone: () -> Unit
    ) {
        val binding = DialogWatchAdsBinding.inflate(LayoutInflater.from(activity))
        val alertDialog = AlertDialog.Builder(activity)
            .setView(binding.root)
            .show()
        alertDialog.window?.setBackgroundDrawable(null)

        if (title == "" || des == "") {


            var content = binding.txtContent.text.toString()

            val watchAdsTimesIndex = content.indexOfFirst { it.isDigit() }
            if (watchAdsTimesIndex != -1) {

                content =
                    content.replaceFirst(content[watchAdsTimesIndex], watchAdsTimes.digitToChar())
                binding.txtContent.text = SpannableString(content).apply {
                    this.setSpan(
                        ForegroundColorSpan(
                            Color.parseColor("#33A7D9")
                        ),
                        watchAdsTimesIndex,
                        watchAdsTimesIndex + 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }

            }
        } else {
            binding.txtContent.text =
                activity.getString(R.string.watch_an_ad_to_unlock_1_extra_challenge)
            binding.tvTitle.text = activity.getString(R.string.more_challenges_unlock_now)
        }


        binding.ivClose.setOnClickListener {
            alertDialog.dismiss()
        }

        binding.txtWatchAds.setOnClickListener {
            AdMobUtils.showRewardAds(activity, object :
                FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    Helper.freeIAP.rewardTimes++
                    AdMobUtils.createRewardAds(
                        activity,
                        activity.getString(R.string.rewardsAdsId),
                        object : AdMobUtils.Companion.LoadAdCallback {
                            override fun onLoaded(ad: Any?) {
                                Helper.isFirstLoadRewardAds = true
                            }

                            override fun onLoadFailed() {

                            }
                        })
                    watchAdsDone.invoke()
                }
            })
            alertDialog.dismiss()

        }

        binding.txtGoAdFree.setOnClickListener {
            val intent = Intent(activity, SubscriptionsActivity::class.java)
            activity.startActivity(intent)
            alertDialog.dismiss()
        }

    }
}