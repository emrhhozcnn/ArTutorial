package com.invio.artutorial

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

/*
*
* created by emrhhozcnn at 12/07/2020
*
* */
class MainActivity : AppCompatActivity() {

    companion object {
        const val CACHED_MODEL_FILE_NAME = "cachedAssetBundle"
        const val KEY_LAST_SAVED_URL = "KEY_LAST_SAVED_URL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setViews()
        initViews()
    }

    private fun setViews() {
        val lastUrl = Utils.getLastSavedModelUrl(this, KEY_LAST_SAVED_URL)
        lastUrl?.let { safeLastUrl ->
            etMainAssetBundleUrl.setText(safeLastUrl)
        }

        if (Utils.isFileExist(this, CACHED_MODEL_FILE_NAME)) {
            tvMainAssetBundleStatus.visibility = View.VISIBLE
            if (etMainTargetImageUrl.text.startsWith("http")) {
                btnMainPlay.isEnabled = true
            }
        }
    }

    private fun initViews() {
        btnMainLoadAssetBundle.setOnClickListener {
            val url = etMainAssetBundleUrl.text.toString()
            url.isNotBlank().let {
                val lastUrl = Utils.getLastSavedModelUrl(this, KEY_LAST_SAVED_URL)
                when {
                    url == lastUrl -> {
                        Toast.makeText(
                            this,
                            R.string.main_load_asset_bundle_already_downloaded,
                            Toast.LENGTH_LONG
                        )
                    }
                    url.isNotBlank() && url.startsWith("http") -> {
                        btnMainLoadAssetBundle.isEnabled = false
                        btnMainLoadAssetBundle.setText(R.string.main_load_asset_bundle_loading)
                        //Download Search Image
                        AsyncImageDownloader(
                            this,
                            CACHED_MODEL_FILE_NAME,
                            url,
                            object : AsyncImageDownloader.ResultListener {
                                override fun actionCompleted(isSuccess: Boolean) {
                                    runOnUiThread {
                                        btnMainPlay.isEnabled = isSuccess
                                        btnMainLoadAssetBundle.isEnabled = true
                                        tvMainAssetBundleStatus.visibility = View.VISIBLE
                                        btnMainLoadAssetBundle.setText(R.string.main_load_asset_bundle)
                                        val errorTextResId = if (isSuccess) R.string.main_load_asset_bundle_success else R.string.main_load_asset_bundle_fail
                                        Toast.makeText(this@MainActivity, errorTextResId, Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        ).execute()
                    }
                    else -> {
                    }
                }
            }
        }

        btnMainPlay.setOnClickListener {
            val arIntent = Intent(this, ArPlayerActivity::class.java)
            arIntent.putExtra(ArPlayerActivity.KEY_IMAGE_URL, etMainTargetImageUrl.text.toString())
            arIntent.putExtra(ArPlayerActivity.KEY_MODEL_PATH, "$filesDir/$CACHED_MODEL_FILE_NAME")
            startActivity(arIntent)
        }
    }
}
