package com.invio.artutorial

import android.content.ComponentCallbacks2
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.unity3d.player.UnityPlayer
import kotlinx.android.synthetic.main.activity_ar_player.*

class ArPlayerActivity : AppCompatActivity() {
    companion object {
        const val KEY_IMAGE_URL = "KEY_IMAGE_URL"
        const val KEY_MODEL_PATH = "KEY_MODEL_PATH"
    }

    private var mUnityPlayer: CustomUnityPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar_player)

        mUnityPlayer = CustomUnityPlayer(this)
        containerArUnityPlayer.addView(mUnityPlayer)
        mUnityPlayer?.requestFocus()

        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    // Quit Unity
    override fun onDestroy() {
        mUnityPlayer?.destroy()
        super.onDestroy()
    }

    // Pause Unity
    override fun onPause() {
        super.onPause()
        mUnityPlayer?.pause()
    }

    // Resume Unity
    override fun onResume() {
        super.onResume()
        mUnityPlayer?.resume()
    }

    // Low Memory Unity
    override fun onLowMemory() {
        super.onLowMemory()
        mUnityPlayer?.lowMemory()
    }

    // Trim Memory Unity
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level == ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL) {
            mUnityPlayer?.lowMemory()
        }
    }

    // Notify Unity of the focus change.
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        mUnityPlayer?.windowFocusChanged(hasFocus)
    }

    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        return if (event.action == KeyEvent.ACTION_MULTIPLE) mUnityPlayer?.injectEvent(
            event
        ) ?: false else super.dispatchKeyEvent(event)
    }

    /*API12*/
    override fun onGenericMotionEvent(event: MotionEvent?): Boolean {
        return mUnityPlayer?.injectEvent(event) ?: false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    // Methods called by Unity
    fun getModelLocation(): String? {
        return intent.extras?.getString(KEY_MODEL_PATH)
    }

    fun getImageUrl(): String? {
        return intent.extras?.getString(KEY_IMAGE_URL)
    }

    fun onModelImportFailed() {
        Log.d("Ar Tutorial", "onModelImportFailed")
    }

    fun onImageLoadingFailed() {
        Log.d("Ar Tutorial", "onImageLoadingFailed")
    }

    fun onDataLoadCompleted() {
        Log.d("Ar Tutorial", "onDataLoadCompleted")
    }

    fun onModelClicked() {
        Log.d("Ar Tutorial", "onModelClicked")
    }

    fun onAnimationCompleted() {
        Log.d("Ar Tutorial", "onAnimationCompleted")
    }
}

class CustomUnityPlayer(context: Context) : UnityPlayer(context) {
    override fun kill() {
    }
}