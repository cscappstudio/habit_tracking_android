package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.adjust.sdk.Adjust
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.databinding.ActivityOnboardBinding
import com.cscmobi.habittrackingandroid.databinding.ActivitySubscritptionsBinding
import com.cscmobi.habittrackingandroid.thanhlv.adapter.SlideSubsAdapter
import com.cscmobi.habittrackingandroid.thanhlv.model.OnBoardModel
import com.google.android.material.tabs.TabLayoutMediator
import com.google.common.collect.ImmutableList
import com.google.gson.JsonObject
import com.thanhlv.ads.lib.AdjustHelper
import com.thanhlv.fw.constant.AppConfigs
import com.thanhlv.fw.helper.FirebaseHelper
import com.thanhlv.fw.helper.MyUtils
import com.thanhlv.fw.helper.MyUtils.Companion.rippleEffect
import com.thanhlv.fw.helper.NetworkHelper
import com.thanhlv.fw.model.LogEventPurchaseModel
import com.thanhlv.fw.spf.SPF
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

class SubscriptionsActivity : BaseActivity2() {

    private lateinit var binding: ActivitySubscritptionsBinding

    override fun setupScreen() {
        binding = ActivitySubscritptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initView() {

        binding.btnNext.rippleEffect()
        binding.vpOnboard.clipToPadding = false
        binding.vpOnboard.clipChildren = false
        binding.vpOnboard.isUserInputEnabled = false
        binding.vpOnboard.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val mAdapter = SlideSubsAdapter(this, getListPhoto())
        binding.vpOnboard.adapter = mAdapter
        TabLayoutMediator(binding.indicator, binding.vpOnboard) { _, _ -> }.attach()
    }

    private fun getListPhoto(): ArrayList<OnBoardModel> {
        val list = ArrayList<OnBoardModel>()
        list.add(
            OnBoardModel(R.drawable.img_onboard_1, "Unlimited habits", "free user can only have 3 habits/day. Upgrade to Premium for no limit, just habit")
        )
        list.add(
            OnBoardModel(R.drawable.img_onboard_2, "Advanced statistics", "Unlock more graph for weekly, monthly and yearly performance")
        )
        list.add(
            OnBoardModel(R.drawable.img_onboard_3, "Custom challenges", "Create your own challenge and tailor habits to fit your life")
        )
        list.add(
            OnBoardModel(R.drawable.img_onboard_4, "No ads", "Zero distractions, 100% focus! Enjoy an ad-free experience with Premium.")
        )
        return list
    }

    private val timer = Timer() // This will create a new Thread
    var ii = 0
    override fun controllerView() {

        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable {
            if (ii > 3) {
                ii = -1
            }
            binding.vpOnboard.setCurrentItem(ii++, true)
        }


        timer.schedule(object : TimerTask() {
            // task to be scheduled
            override fun run() {
                handler.post(runnable)
            }
        }, 100, 1200)

        binding.btnNext.setOnClickListener {
            startActivity(Intent(this, QuestionFOActivity::class.java))
            finish()
        }
        binding.vpOnboard.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            @SuppressLint("UseCompatLoadingForDrawables")
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                ii = position
            }
        })
    }

    override fun loadData() {
    }

    private fun trackingPurchase(purchase: Purchase, productDetail: ProductDetails) {
        //tracking Firebase
        var price: Long = 0
        var ss = ""
        if (productDetail.productType == "inapp" && productDetail.oneTimePurchaseOfferDetails != null) {
            price = productDetail.oneTimePurchaseOfferDetails!!.priceAmountMicros
            ss = productDetail.oneTimePurchaseOfferDetails!!.formattedPrice
        } else if (productDetail.productType == "subs" && productDetail.subscriptionOfferDetails != null
            && productDetail.subscriptionOfferDetails!!.isNotEmpty()
        ) {
            ss =
                getPrice(productDetail.subscriptionOfferDetails!![0].pricingPhases.pricingPhaseList)
            price = maxPrice
        }
        val eventPurchaseModel =
            LogEventPurchaseModel(SELECTED_SUBS, "default", (price / 1000000).toDouble())
        FirebaseHelper.getInstance(this).logEventPurchaseToFirebase(eventPurchaseModel)
        SPF.setCurrentSub(this, purchase.products[0] + "--" + ss)
        //tracking GSM
        val params = JsonObject()
        params.addProperty("packageName", packageName)
        params.addProperty("purchaseToken", purchase.purchaseToken)
        params.addProperty("productId", productDetail.productId)
        params.addProperty("productName", productDetail.name)
        params.addProperty("rechargePoint", "0")
        params.addProperty("keyhash", MyUtils.getKeyHash(this))
        params.addProperty("adid", Adjust.getAdid())
        params.addProperty("userType", "0")
        if (productDetail.productType == "inapp") GSMUtil.verifyIAP(this, params, null)
        if (productDetail.productType == "subs") GSMUtil.verifySUBS(this, params, null)

        //tracking Adjust
        AdjustHelper.trackingSubscription(purchase, productDetail)
    }

    private var productDetails = CopyOnWriteArrayList<ProductDetails?>()

    private fun querySubs() {
        val productList: ImmutableList<QueryProductDetailsParams.Product> = ImmutableList.of(
            QueryProductDetailsParams.Product.newBuilder().setProductId(yearlyID)
                .setProductType(BillingClient.ProductType.SUBS).build(),
            QueryProductDetailsParams.Product.newBuilder().setProductId(weeklyID)
                .setProductType(BillingClient.ProductType.SUBS).build()
        )
        val params = QueryProductDetailsParams.newBuilder().setProductList(productList).build()
        billingClient.queryProductDetailsAsync(
            params
        ) { _: BillingResult?, prodDetailsList: List<ProductDetails?> ->
            // Process the result
            productDetails = CopyOnWriteArrayList()
            if (prodDetailsList.isNotEmpty()) {
                productDetails.addAllAbsent(prodDetailsList)
            }
            queryInApp()
        }
    }

    private fun queryInApp() {
        val productListInApp: ImmutableList<QueryProductDetailsParams.Product> = ImmutableList.of(
            QueryProductDetailsParams.Product.newBuilder().setProductId(lifetimeID)
                .setProductType(BillingClient.ProductType.INAPP).build()
        )
        val paramsInApp =
            QueryProductDetailsParams.newBuilder().setProductList(productListInApp).build()
        billingClient.queryProductDetailsAsync(
            paramsInApp
        ) { _: BillingResult?, prodDetailsList: List<ProductDetails?> ->
            if (prodDetailsList.isNotEmpty()) productDetails.addAllAbsent(prodDetailsList)
            runOnUiThread { fillSubscription() }
        }
    }



//    @SuppressLint("SetTextI18n")
//    fun fillSubscription() {
//        try {
//            var filled = 0
//            listSubs.clear()
//            for (i in productDetails.indices) {
//                if (productDetails[i] == null) return
//                val id = productDetails[i]!!.productId
//                var price: String? = ""
//                if (productDetails[i]!!.productType == "inapp" && productDetails[i]!!.oneTimePurchaseOfferDetails != null) {
//                    price = productDetails[i]!!.oneTimePurchaseOfferDetails!!.formattedPrice
//                } else if (productDetails[i]!!.productType == "subs" && productDetails[i]!!
//                        .subscriptionOfferDetails != null && productDetails[i]!!.subscriptionOfferDetails!!.isNotEmpty()
//                ) price =
//                    getPrice(productDetails[i]!!.subscriptionOfferDetails!![0].pricingPhases.pricingPhaseList)
//                if (id == weeklyID) {
//                    listSubs.add(
//                        SubsModel(
//                            0,
//                            getString(R.string.weekly),
//                            price!!,
//                            getString(R.string.automatically_renewed),
//                            false,
//                            id
//                        )
//                    )
//                    filled++
//                }
//                if (id == yearlyID) {
//                    listSubs.add(
//                        SubsModel(
//                            1,
//                            getString(R.string.yearly),
//                            price!!,
//                            getString(R.string.automatically_renewed),
//                            true,
//                            id
//                        )
//                    )
//                    filled++
//                }
//                if (id == lifetimeID) {
//                    listSubs.add(
//                        SubsModel(
//                            2,
//                            getString(R.string.lifetime),
//                            price!!,
//                            getString(R.string.once_payment),
//                            false,
//                            id
//                        )
//                    )
//                    filled++
//                }
//            }
//            listSubs.sortWith { o1, o2 -> o1.index.compareTo(o2.index) }
//            if (listSubs.size == 3) {
//                subsAdapter?.updateData(listSubs)
//                binding.slider.isUserInputEnabled = true
////                binding.slider.setCurrentItem(1, true)
//
//                Handler(Looper.getMainLooper()).postDelayed({
//                    binding.slider.setCurrentItem(1, true)
//                }, 200)
//                SELECTED_SUBS = listSubs[1].keyID
//                binding.btnBuy.isEnabled = true
//            }
//        } catch (ignored: Exception) {
//        }
//    }




    private var maxPrice: Long = -1
    private var currencyCode = ""
    private var formatPrice = ""

    private fun getPrice(pricingPhaseList: List<ProductDetails.PricingPhase>): String {
        maxPrice = -1
        formatPrice = ""
        for (pricingPhase in pricingPhaseList) {
            if (pricingPhase.priceAmountMicros > maxPrice) {
                maxPrice = pricingPhase.priceAmountMicros
                formatPrice = pricingPhase.formattedPrice
                currencyCode = pricingPhase.priceCurrencyCode
            }
        }
        return formatPrice
    }

    private fun connectGooglePlayBilling() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) querySubs()
            }

            override fun onBillingServiceDisconnected() {
                runOnUiThread {
                    if (NetworkHelper.isNetworkAvailable(applicationContext))
                        Toast.makeText(
                            this@SubscriptionsActivity,
                            getString(R.string.billing_is_not_connected_please_try_again),
                            Toast.LENGTH_LONG
                        ).show()
                }
            }
        })
    }


    private var monthlyID = AppConfigs.KEY_SUBS_MONTHLY
    private var yearlyID = AppConfigs.KEY_SUBS_YEARLY
    private var lifetimeID = AppConfigs.KEY_SUBS_LIFETIME


    private lateinit var billingClient: BillingClient
    private var productDetail: ProductDetails? = null


    fun launchPurchaseFlow(productID: String) {
        if (SELECTED_SUBS == "") return
        for (i in productDetails.indices) {
            if (productDetails[i]!!.productId == productID) {
                productDetail = productDetails[i]
                break
            }
        }
        if (productDetail == null) return
        var token: String? = null
        if (productDetail!!.subscriptionOfferDetails != null) {
            token = productDetail!!.subscriptionOfferDetails!![0].offerToken
        }
        val productDetailsParamsList: ImmutableList<BillingFlowParams.ProductDetailsParams> = if (token == null) {
            ImmutableList.of(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(productDetail!!)
                    .build()
            )
        } else {
            ImmutableList.of(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(productDetail!!)
                    .setOfferToken(token)
                    .build()
            )
        }
        val billingFlowParams =
            BillingFlowParams.newBuilder().setProductDetailsParamsList(productDetailsParamsList)
                .build()
        billingClient.launchBillingFlow(this@SubscriptionsActivity, billingFlowParams)
    }


    override fun onResume() {
        super.onResume()
        MyUtils.hideNavigationBar(this)
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.SUBS).build()
        ) { billingResult: BillingResult, list: List<Purchase> ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && list.isNotEmpty())
                for (purchase in list)
                    if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged)
                        handlePurchase(purchase)

        }

        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.INAPP)
                .build()
        ) { billingResult2: BillingResult, list2: List<Purchase> ->
            if (billingResult2.responseCode == BillingClient.BillingResponseCode.OK && list2.isNotEmpty())
                for (purchase in list2)
                    if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged)
                        handlePurchase(purchase)

        }
    }
//    @SuppressLint("MissingSuperCall")
//    @Deprecated("Deprecated in Java")
//    override fun onBackPressed() {
//    }

}