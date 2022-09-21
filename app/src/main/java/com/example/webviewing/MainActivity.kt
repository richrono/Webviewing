package com.example.webviewing

import android.accessibilityservice.GestureDescription
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val url = "https://www.yahoo.co.jp/"
        //ボタン１つめはCustomTabで開く
        val btnWebViewActivity: Button = findViewById(R.id.btnWebView)
        btnWebViewActivity.setOnClickListener {
            openCustomTab(this, Uri.parse(url))
        }
        //ボタン２つめは単純なWebViewを別アクティビティで開く
        val btnWebViewActivity2: Button = findViewById(R.id.btnWebView2)
        btnWebViewActivity2.setOnClickListener {
            val intent = Intent(applicationContext, WebViewActivity::class.java)
            startActivity(intent)
        }
        //ボタン３つめはカスタマイズしたWebViewを別アクティビティで開く
        val btnWebViewActivity3: Button = findViewById(R.id.btnWebView3)
        btnWebViewActivity2.setOnClickListener {
            val intent = Intent(applicationContext, CustomizedWebViewActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * CustomTabsをサポートするブラウザがあるかどうかを探す
     */
    private fun getCustomTabsPackages(context: Context): List<ResolveInfo> {
        val pm = context.packageManager
        val activityIntent = Intent()
            .setAction(Intent.ACTION_VIEW)
            .addCategory(Intent.CATEGORY_BROWSABLE)
            .setData(Uri.fromParts("http", "", null))
        val resolvedActivityList =
            pm.queryIntentActivities(activityIntent, PackageManager.MATCH_ALL)
        return resolvedActivityList.mapNotNull { info ->
            val serviceIntent = Intent()
            serviceIntent.action = ACTION_CUSTOM_TABS_CONNECTION
            serviceIntent.setPackage(info.activityInfo.packageName)
            // Custom TabsのServiceが解決できるResolveInfoを取得する
            if (pm.resolveService(serviceIntent, 0) != null) {
                return@mapNotNull info
            }
            return@mapNotNull null
        }.toList()
    }

    private val chromePackages = listOf(
        // stable
        "com.android.chrome",
        // beta
        "com.chrome.beta",
        // dev
        "com.chrome.dev",
        // canary
        "com.chrome.canary",
        // local
        "com.google.android.apps.chrome"
    )

    /**
     * Chromeがある場合はChromeのパッケージを返す
     */
    private fun getChromePackages(context: Context): String? {
        val customTabsPackages = getCustomTabsPackages(context)
        return chromePackages.find { chromePackage ->
            customTabsPackages.any { it.activityInfo.packageName == chromePackage }
        }
    }

    private fun openCustomTab(context: Context, uri: Uri) {
        val customTabsPackages = getCustomTabsPackages(context)
        val chromePackage = getChromePackages(context)
        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(true)
//                .setToolbarColor(getColor(R.color.colorPrimary))
            .build()
        if (chromePackage != null) {
            // Chromeがある場合はChromeを優先的に開く
            customTabsIntent.intent.apply {
                setPackage(chromePackage)
            }
        } else if (customTabsPackages.isNotEmpty()) {
            // Chromeは無いがCustom Tabs対応ブラウザがある場合は最初の1件を自動で選択してCustom Tabsを開く
            customTabsIntent.intent.apply {
                setPackage(customTabsPackages[0].activityInfo.packageName)
            }
        }

        customTabsIntent.launchUrl(context, uri)
    }


//        val btnWebViewActivity : Button = findViewById(R.id.btnWebView)
//        btnWebViewActivity.setOnClickListener {
//            val url = "https://www.yahoo.co.jp/"
//            val builder : CustomTabsIntent.Builder = CustomTabsIntent.Builder()
//            val customTabsIntent: CustomTabsIntent = builder.build()
//            customTabsIntent.launchUrl(this, Uri.parse(url))
//        }

}