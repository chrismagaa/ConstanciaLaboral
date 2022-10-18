package com.camgapps.constancia_laboral

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import java.security.PrivateKey

object AdMobManager {


    private var mInterstitialAd: InterstitialAd? = null
    const val idIntersticial = "ca-app-pub-4702215318330979/6730449051"
    private val TAG = "AdmobManager"


    fun loadAd(activity: Activity) {
        if (mInterstitialAd == null) {
            var adRequest = AdRequest.Builder().build()

            InterstitialAd.load(
                activity, idIntersticial, adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        Log.d("Intersticial", adError.message)
                        mInterstitialAd = null
                    }

                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        Log.d("Intersticial", "Ad was loaded.")
                        mInterstitialAd = interstitialAd
                    }
                }
            )
        }else{
            Log.d(this@AdMobManager.javaClass.simpleName, "Ya estaba cargado")
        }
    }


    fun showAd(activity: Activity, onShowAd: () -> Unit) {
        if (mInterstitialAd != null) {
            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    mInterstitialAd = null
                    onShowAd()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                    mInterstitialAd = null
                    onShowAd()
                }

                override fun onAdShowedFullScreenContent() {
                    mInterstitialAd = null
                }
            }
            mInterstitialAd?.show(activity)
        }else{
            onShowAd()
        }
    }


    private var mRewardedAd: RewardedAd? = null
    private var idRewardedAd = "ca-app-pub-4702215318330979/1186328937"

    fun loadRecompensate(activity: Activity, onPositive: () -> Unit, onCancel: () -> Unit) {
        var adRequest = AdRequest.Builder().build()

        RewardedAd.load(activity, idRewardedAd, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError?.toString())
                mRewardedAd = null
                onCancel()
            }

            override fun onAdLoaded(rewardedAd: RewardedAd) {
                Log.d(TAG, "Ad was loaded.")
                mRewardedAd = rewardedAd
                showRecompensate(activity, onPositive, onCancel)
            }
        })

    }



    fun showRecompensate(activity: Activity, onShow: () -> Unit, onCancel: () -> Unit){
        if (mRewardedAd != null) {
            mRewardedAd?.show(activity, OnUserEarnedRewardListener() {
                Log.d(TAG, "User earned the reward.")
                onShow()
            })
        } else {
            onCancel
            Log.d(TAG, "The rewarded ad wasn't ready yet.")
        }
    }


}