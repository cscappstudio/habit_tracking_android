package com.thanhlv.fw.helper

import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.*
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.RippleDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.MediaStore
import android.provider.Settings.Secure
import android.text.TextUtils
import android.util.Base64
import android.view.*
import android.view.animation.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.viewpager2.widget.ViewPager2
import com.android.billingclient.api.*
import com.google.android.gms.ads.MobileAds
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.thanhlv.ads.lib.AdjustHelper.Companion.trackingEvent
import com.thanhlv.ads.lib.AdsConfigs
import com.thanhlv.ads.lib.AdsConfigs.*
import com.thanhlv.ads.lib.AdsConfigs.Companion.ADJUST_WATCH_10_ADS_FULL_TOKEN
import com.thanhlv.ads.lib.AdsConfigs.Companion.ADJUST_WATCH_5_ADS_FULL_TOKEN
import com.thanhlv.fw.constant.AppConfigs.Companion.EVENT_WATCH_10_ADS_FULL
import com.thanhlv.fw.constant.AppConfigs.Companion.EVENT_WATCH_5_ADS_FULL
import com.thanhlv.fw.constant.AppConfigs.Companion.adFullShowTime
import com.thanhlv.fw.remoteconfigs.RemoteConfigs
import com.thanhlv.fw.spf.SPF
import org.json.JSONObject
import java.io.File
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


class MyUtils {

    companion object {

        fun getAppStorageDirectory(context: Context): String? {
            val directory = File(context.filesDir, "CSCARRuler")
            if (!directory.exists()) {
                val a = directory.mkdirs()
            }
            return directory.absolutePath
        }

        fun hideNavigationBar(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                activity.window.insetsController!!.hide(WindowInsets.Type.navigationBars())
                activity.window.insetsController!!.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
            activity.window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }

        fun hideStatusBar(activity: Activity) {
            activity.window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
            setFullscreen(activity)
        }

        fun changeStatusBarColor(activity: Activity, color: Int) {
            if (activity.isFinishing) return
            val window = activity.window ?: return
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = color
        }

        private fun setFullscreen(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                activity.window.attributes.layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                activity.window.setDecorFitsSystemWindows(false)
                activity.window.insetsController!!.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                activity.window.insetsController!!.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            } else {
                activity.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
            }
        }

        fun hideSoftInput(activity: Activity) {
            hideSoftInput(activity.window)
        }

        private fun hideSoftInput(window: Window) {
            var view = window.currentFocus
            if (view == null) {
                val decorView = window.decorView
                val focusView = decorView.findViewWithTag<View>("keyboardTagView")
                if (focusView == null) {
                    view = EditText(window.context)
                    view.setTag("keyboardTagView")
                    (decorView as ViewGroup).addView(view, 0, 0)
                } else {
                    view = focusView
                }
                view.requestFocus()
            }
            hideSoftInput(view)
        }

        fun showSoftInput(view: View) {
            val imm =
                view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, 0)
        }


        fun hideSoftInput(view: View) {
            val imm =
                view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    ?: return
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        fun configKeyboardBelowEditText(activity: Activity) {
            val window = activity.window
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
            )
            window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.TYPE_STATUS_BAR
            )
        }

        @SuppressLint("DefaultLocale")
        fun parseLongToTime(durationInMillis: Long): String? {
            val second = durationInMillis / 1000 % 60
            val minute = durationInMillis / (1000 * 60) % 60
            val hour = durationInMillis / (1000 * 60 * 60) % 24
            return if (hour == 0L) String.format(
                "%02d:%02d",
                minute,
                second
            ) else String.format("%02d:%02d:%02d", hour, minute, second)
        }

        fun showSnackBarNotification(view: View?, msg: String?, length: Int) {
            Snackbar.make(view!!, msg!!, length).show()
        }


        fun getExtensionFile(path: String): String {
            return if (TextUtils.isEmpty(path)) "" else path.substring(path.lastIndexOf("."))
        }

        fun getFileNameWithoutExtension(path: String): String {
            if (TextUtils.isEmpty(path)) return ""
            val ext = path.substring(path.lastIndexOf("."))
            val file = File(path)
            return file.name.replace(ext, "")
        }

        fun isMyServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (service in manager.getRunningServices(Int.MAX_VALUE)) {
                if (serviceClass.name == service.service.className) {
                    return true
                }
            }
            return false
        }


        fun getKeyHash(context: Context): String? {
            val packageInfo: PackageInfo
            var key: String? = null
            try {
                //getting application package name, as defined in manifest
                val packageName = context.applicationContext.packageName

                //Retriving package info
                packageInfo = context.packageManager.getPackageInfo(
                    packageName,
                    PackageManager.GET_SIGNATURES
                )
                println("thanhlv ================================================================")
                println("thanhlv ====== Package Name: " + context.applicationContext.packageName)
                for (signature in packageInfo.signatures) {
                    val md = MessageDigest.getInstance("SHA")
                    md.update(signature.toByteArray())
                    key = String(Base64.encode(md.digest(), 0))

                    // String key = new String(Base64.encodeBytes(md.digest()));
                    println("thanhlv ====== Key Hash: $key")
                }
            } catch (e1: PackageManager.NameNotFoundException) {
                println("thanhlv ======== Name not found$e1")
            } catch (e: NoSuchAlgorithmException) {
                println("thanhlv ======== No such an algorithm$e")
            } catch (e: Exception) {
                println("thanhlv ======== Exception$e")
            }
            println("thanhlv ================================================================")
            return key
        }

        @SuppressLint("HardwareIds")
        fun getDeviceID(context: Context): String? {
            return Secure.getString(context.contentResolver, Secure.ANDROID_ID)
        }

        fun getSpanCount(widthDpItem: Int): Int {
            return (DisplayUtils.getDeviceWidthDpi() / widthDpItem + 1).toInt()
//            return if (DisplayUtils.getDeviceWidthDpi() > 600) 4 else 3
        }

        @SuppressLint("ClickableViewAccessibility")
        fun View.rippleEffect() {
            setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        v.foreground =
                            RippleDrawable( //Using the foreground allows you to give the view whatever background you need
                                ColorStateList.valueOf(0xFFFFFFFF.toInt()),
                                null,//Whatever shape you put here will cover everything you've got underneath so you probably want to keep it "null"
                                v.background
                            )
                        v.foreground.setHotspot(event.x, event.y)
                    }
                }
                false
            }
            isClickable = true
        }

        @SuppressLint("ClickableViewAccessibility")
        fun View.rippleEffect(color: String) {
            setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        v.foreground =
                            RippleDrawable( //Using the foreground allows you to give the view whatever background you need
                                ColorStateList.valueOf(Color.parseColor(color)),
                                null,//Whatever shape you put here will cover everything you've got underneath so you probably want to keep it "null"
                                v.background
                            )
                        v.foreground.setHotspot(event.x, event.y)
                    }
                }
                false
            }
            isClickable = true
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun View.ringEffect() {
            try {
                val vibration = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibration.vibrate(
                    VibrationEffect.createOneShot(
                        50,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } catch (_: Exception) {
            }
        }

        fun View.blinking() {
            val animation: Animation = AlphaAnimation(1f, 0f)
            animation.duration = 500
            animation.interpolator = LinearInterpolator()
            animation.repeatCount = Animation.ABSOLUTE
            animation.repeatMode = Animation.REVERSE
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(p0: Animation?) {
                    visibility = View.VISIBLE
                }

                override fun onAnimationEnd(p0: Animation?) {
                    visibility = View.GONE
                }

                override fun onAnimationRepeat(p0: Animation?) {
                }
            })
            startAnimation(animation)
        }

        fun saveImageToGallery(context: Context, imagePath: String) {
            val fileName = File(imagePath).nameWithoutExtension
            val bitmap = BitmapFactory.decodeFile(imagePath)

            val imageUri: Uri?
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "$fileName.jpg")
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                bitmap?.let {
                    put(MediaStore.Images.Media.WIDTH, bitmap.width)
                    put(MediaStore.Images.Media.HEIGHT, bitmap.height)
                }
            }

            imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES + File.separator.toString() + "ARRuler"
                )
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }

            try {
                val uri = context.contentResolver.insert(imageUri, contentValues)
                val fos = uri?.let {
                    context.contentResolver.openOutputStream(it)
                }
                if (fos != null) {
                    bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                }
                Objects.requireNonNull(fos)
            } catch (_: Exception) {
                Toast.makeText(
                    context,
                    String.format("context.getString(R.string.download_file_s_error)", fileName),
                    Toast.LENGTH_LONG
                ).show()
            } finally {
                Toast.makeText(
                    context,
                    String.format("context.getString(R.string.file_s_downloaded)", fileName),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        fun scaleAnim(v: View, duration: Int, expand: Boolean) {
            var scaleAnimation = ScaleAnimation(3f, 1f, 0f, 1f, 0f, 0f)
            if (!expand) scaleAnimation = ScaleAnimation(1f, 2f, 1f, 0f, 0f, 0f)
            scaleAnimation.duration = duration.toLong()
            scaleAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    v.visibility = View.VISIBLE
                }

                override fun onAnimationEnd(animation: Animation) {
                    if (expand) v.visibility = View.VISIBLE else v.visibility = View.GONE
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
            v.startAnimation(scaleAnimation)
        }

        fun ViewPager2.setCurrentItem(
            item: Int,
            duration: Long,
        ) {
            val interpolator: TimeInterpolator = AccelerateDecelerateInterpolator()
            val pagePxWidth: Int = width // Default value taken from getWidth() from ViewPager2 view
            val pxToDrag: Int = pagePxWidth * (item - currentItem)
            val animator = ValueAnimator.ofInt(0, pxToDrag)
            var previousValue = 0
            animator.addUpdateListener { valueAnimator ->
                val currentValue = valueAnimator.animatedValue as Int
                val currentPxToDrag = (currentValue - previousValue).toFloat()
                fakeDragBy(-currentPxToDrag)
                previousValue = currentValue
            }
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    beginFakeDrag()
                }

                override fun onAnimationEnd(animation: Animator) {
                    endFakeDrag()
                }

                override fun onAnimationCancel(animation: Animator) { /* Ignored */
                }

                override fun onAnimationRepeat(animation: Animator) { /* Ignored */
                }
            })
            animator.interpolator = interpolator
            animator.duration = duration
            animator.start()
        }

        fun logEventShowAdFull(context: Context) {
            val count = SPF.getCountShowAdFull(context) + 1
            if (count == 5) {
                trackingEvent(ADJUST_WATCH_5_ADS_FULL_TOKEN)
                FirebaseHelper.getInstance(context)
                    .logEventToFirebase(EVENT_WATCH_5_ADS_FULL)
            }
            if (count == 10) {
                trackingEvent(ADJUST_WATCH_10_ADS_FULL_TOKEN)
                FirebaseHelper.getInstance(context)
                    .logEventToFirebase(EVENT_WATCH_10_ADS_FULL)
            }
            SPF.setCountShowAdFull(context, count)
        }
        fun parserTimeFromMs(milis: Long): String {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = milis
            return "" + calendar.time.hours + ":" + calendar.time.minutes
        }


        @SuppressLint("DefaultLocale")
        fun parserLong2Time(durationInMillis: Long): String {
            val second = durationInMillis / 1000 % 60
            val minute = durationInMillis / (1000 * 60) % 60
            val hour = durationInMillis / (1000 * 60 * 60) % 24
            return if (hour == 0L) String.format(
                "%02d:%02d",
                minute,
                second
            ) else String.format("%02d:%02d:%02d", hour, minute, second)
        }


        fun expandView(v: View, durationMs: Int, targetHeightDp: Int) {
            val prevHeight = v.height
            v.visibility = View.VISIBLE
            val valueAnimator = ValueAnimator.ofInt(prevHeight, DisplayUtils.dpToPx(targetHeightDp.toFloat()))
            valueAnimator.addUpdateListener { animation ->
                v.layoutParams.height = animation.animatedValue as Int
                v.requestLayout()
            }
            valueAnimator.interpolator = DecelerateInterpolator()
            valueAnimator.duration = durationMs.toLong()
            valueAnimator.start()
        }

        fun collapseView(v: View, durationMs: Int, targetHeightDp: Int) {
            val prevHeight = v.height
            val valueAnimator = ValueAnimator.ofInt(prevHeight, DisplayUtils.dpToPx(targetHeightDp.toFloat()))
            valueAnimator.interpolator = DecelerateInterpolator()
            valueAnimator.addUpdateListener { animation ->
                v.layoutParams.height = animation.animatedValue as Int
                if (animation.animatedValue as Int == 0) v.visibility = View.GONE
                v.requestLayout()
            }
            valueAnimator.interpolator = DecelerateInterpolator()
            valueAnimator.duration = durationMs.toLong()
            valueAnimator.start()
        }

        fun consumeAllTestPurchases(context: Context) {

            val billingClient = BillingClient.newBuilder(context)
                .enablePendingPurchases()
                .setListener { _, _ -> }
                .build()
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingServiceDisconnected() {}
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        val queryPurchaseHistoryParams: QueryPurchaseHistoryParams =
                            QueryPurchaseHistoryParams.newBuilder()
                                .setProductType(BillingClient.ProductType.SUBS).build()
                        val purchasesParamsSUBS: QueryPurchasesParams =
                            QueryPurchasesParams.newBuilder()
                                .setProductType(BillingClient.ProductType.SUBS).build()
                        val purchasesParamsINAPP: QueryPurchasesParams =
                            QueryPurchasesParams.newBuilder()
                                .setProductType(BillingClient.ProductType.INAPP).build()
                        billingClient.queryPurchaseHistoryAsync(
                            queryPurchaseHistoryParams
                        ) { _, _ ->
                            billingClient.queryPurchasesAsync(
                                purchasesParamsSUBS
                            ) { billingResult11, list1 ->
                                if (billingResult11.responseCode == BillingClient.BillingResponseCode.OK)
                                    list1.forEach {
                                        val consumeParams = ConsumeParams.newBuilder().setPurchaseToken(it.purchaseToken).build()
                                        billingClient.consumeAsync(consumeParams) { billingResult, _ ->
                                            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                                                println("thanhlv consumePurchase subs --- " + consumeParams.purchaseToken)
                                            }
                                        }
                                    }

                                billingClient.queryPurchasesAsync(purchasesParamsINAPP) { billingResult111, list2 ->
                                    if (billingResult111.responseCode == BillingClient.BillingResponseCode.OK)
                                        list2.forEach {
                                            val consumeParams = ConsumeParams.newBuilder().setPurchaseToken(it.purchaseToken).build()
                                            billingClient.consumeAsync(consumeParams) { billingResult, _ ->
                                                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                                                    println("thanhlv consumePurchase inapp --- " + consumeParams.purchaseToken)

                                                }
                                            }
                                        }
                                }
                            }
                        }
                    }
                }
            })
        }

        fun checkIntervalTime(): Boolean {
            val cur = System.currentTimeMillis()
            val interval = RemoteConfigs.instance.getConfigValue(AdsConfigs.KEY_AD_FULL_INTERVAL_TIME).asLong()
            return cur - adFullShowTime >= interval
        }



        open fun shareApp(context: Context) {
//        DogApp.ignoreOpenAd = true
            val sendIntent = Intent(Intent.ACTION_SEND)
            sendIntent.putExtra(
                Intent.EXTRA_TEXT,
                RemoteConfigs.instance.appConfigs.shareText + "\n"
                        + String.format("https://play.google.com/store/apps/details?id=%s", context.packageName)
            )
            sendIntent.type = "text/plain"
            context.startActivity(Intent.createChooser(sendIntent, "Share to"))
        }
        fun showMoreApp(context: Context) {
//        DogApp.ignoreOpenAd = true
            try {
               context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(RemoteConfigs.instance.appConfigs.moreAppURL)
                    )
                )
            } catch (e: ActivityNotFoundException) {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(RemoteConfigs.instance.appConfigs.moreAppURL)
                    )
                )
            }
        }

        fun openDeeplink(context: Context, url: String?) {
            if (url.isNullOrEmpty()) return
//        DogApp.ignoreOpenAd = true
            try {
                val i = Intent(Intent.ACTION_VIEW)
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    i.data = Uri.parse(url)
                } else i.data = Uri.parse("http://$url")
                context.startActivity(i)
            } catch (ignored: java.lang.Exception) {
            }
        }



//        private fun getValueCurrencyList(): MutableList<ValueCurrencyModel> {
//            val list = mutableListOf<ValueCurrencyModel>()
//            val str = readJsonAsset("currency.json")
//            val jsonObject = JSONObject(str)
//            val data = JSONObject(jsonObject.get("data").toString())
//            for (key in data.keys()) {
//                list.add(
//                    Gson().fromJson(
//                        JSONObject(data[key].toString()).toString(),
//                        ValueCurrencyModel::class.java
//                    )
//                )
//            }
//            return list
//        }

        private fun readJsonAsset(context: Context, fileName: String): String {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            return String(buffer, Charsets.UTF_8)
        }

    }
}