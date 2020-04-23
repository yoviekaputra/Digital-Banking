package mlpt.siemo.digitalbanking.module.videocall

import android.Manifest
import android.app.AlertDialog
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.opentok.android.*
import kotlinx.android.synthetic.main.activity_video_call_activiy.*
import mlpt.siemo.digitalbanking.MainActivity
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions


class VideoCallActivity : AppCompatActivity(),
    Session.SessionListener,
    PublisherKit.PublisherListener,
    SubscriberKit.SubscriberListener,
    WebServiceCoordinator.Listener {

    override fun onConnected(p0: SubscriberKit?) {
    }

    override fun onDisconnected(p0: SubscriberKit?) {
    }

    override fun onError(p0: SubscriberKit?, p1: OpentokError?) {
    }

    companion object {
        const val API_KEY = "46431132"
        const val SESSION_ID = "1_MX40NjQzMTEzMn5-MTU3NDY4MzU0NTkzMH5neWNBSW9EZzZkRjUrQXh0VmtDeURKL1p-fg"
        const val TOKEN = "T1==cGFydG5lcl9pZD00NjQzMTEzMiZzaWc9Njc1MDdmODgwN2M4OWU4ZTEwN2U3NzdmYmYyZDhkYTQyMDNjNzkxNDpzZXNzaW9uX2lkPTFfTVg0ME5qUXpNVEV6TW41LU1UVTNORFk0TXpVME5Ua3pNSDVuZVdOQlNXOUVaelprUmpVclFYaDBWbXREZVVSS0wxcC1mZyZjcmVhdGVfdGltZT0xNTc0NjgzNTcwJm5vbmNlPTAuODE0NTgzOTU0MzIxNTgwMyZyb2xlPXB1Ymxpc2hlciZleHBpcmVfdGltZT0xNTc3Mjc1NTY5JmluaXRpYWxfbGF5b3V0X2NsYXNzX2xpc3Q9"
        val LOG_TAG = MainActivity::class.java.simpleName
        const val RC_VIDEO_APP_PERM = 124
    }


    private var mSession: Session? = null
    private var mPublisher: Publisher? = null
    private var mSubscriber: Subscriber? = null
    private var mWebServiceCoordinator: WebServiceCoordinator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mlpt.siemo.digitalbanking.R.layout.activity_video_call_activiy)

        requestPermissions()
    }

    override fun onResume() {
        super.onResume()
        mSession?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mSession?.onPause()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private fun requestPermissions() {
        val param = arrayOf(Manifest.permission.INTERNET,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO)

        if (EasyPermissions.hasPermissions(this, *param)) {
            // initialize and connect to the session
            mSession = Session.Builder(this, API_KEY, SESSION_ID).build()
            mSession?.setSessionListener(this)
            mSession?.connect(TOKEN)

            // if there is no server URL set
            if (OpenTokConfig.CHAT_SERVER_URL == null) {
                // use hard coded session values
                if (OpenTokConfig.areHardCodedConfigsValid()) {
                    initializeSession(
                        API_KEY,
                        SESSION_ID,
                        TOKEN
                    )
                } else {
                    showConfigError(
                        "Configuration Error",
                        OpenTokConfig.hardCodedConfigErrorMessage
                    )
                }
            } else {
                // otherwise initialize WebServiceCoordinator and kick off request for session data
                // session initialization occurs once data is returned, in onSessionConnectionDataReady
                if (OpenTokConfig.isWebServerConfigUrlValid()) {
                    mWebServiceCoordinator = WebServiceCoordinator(this, this)
                    mWebServiceCoordinator?.fetchSessionConnectionData(OpenTokConfig.SESSION_INFO_ENDPOINT)
                } else {
                    showConfigError(
                        "Configuration Error",
                        OpenTokConfig.webServerConfigErrorMessage
                    )
                }
            }

        } else {
            EasyPermissions.requestPermissions(
                this,
                "This app needs access to your camera and mic to make video calls",
                RC_VIDEO_APP_PERM, *param
            )
        }
    }

    override fun onConnected(session: Session) {
        Log.i(LOG_TAG, "Session Connected")
        mPublisher = Publisher.Builder(this).build()
        mPublisher?.apply {
            setPublisherListener(this@VideoCallActivity)
            renderer.setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FILL)

            publisher_container.addView(view)
            if (view is GLSurfaceView) {
                (view as GLSurfaceView).setZOrderOnTop(true)
            }
            mSession?.publish(this)
        }
    }

    override fun onSessionConnectionDataReady(apiKey: String?, sessionId: String?, token: String?) {
        initializeSession(apiKey, sessionId, token)
    }

    override fun onWebServiceCoordinatorError(error: Exception?) {
        finish()
    }

    override fun onStreamDropped(p0: Session?, p1: Stream?) {
        mSubscriber?.let {
            mSubscriber = null
            subscriber_container.removeAllViews()
        }
    }

    override fun onStreamReceived(p0: Session?, p1: Stream?) {
        mSubscriber?.apply {
            mSubscriber = Subscriber.Builder(this@VideoCallActivity, p1).build()
            renderer.setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FILL)
            p0?.subscribe(this)
            subscriber_container.addView(this.view)
        }
    }

    override fun onDisconnected(session: Session) {
        Log.i(LOG_TAG, "Session Disconnected")
    }

    override fun onError(session: Session, opentokError: OpentokError) {
        Log.e(LOG_TAG, "Session error: " + opentokError.message)
    }

    override fun onError(p0: PublisherKit?, p1: OpentokError?) {
        showOpenTokError(p1)
    }

    override fun onStreamCreated(p0: PublisherKit?, p1: Stream?) {
    }

    override fun onStreamDestroyed(p0: PublisherKit?, p1: Stream?) {
    }

    private fun initializeSession(apiKey: String?, sessionId: String?, token: String?) {
        mSession = Session.Builder(this, apiKey, sessionId).build()
        mSession?.setSessionListener(this)
        mSession?.connect(token)
    }

    private fun showOpenTokError(opentokError: OpentokError?) {

        Toast.makeText(
            this,
            opentokError?.errorDomain?.name + ": " + opentokError?.message + " Please, see the logcat.",
            Toast.LENGTH_LONG
        ).show()
        finish()
    }
    private fun showConfigError(alertTitle: String, errorMessage: String) {
        Log.e(LOG_TAG, "Error $alertTitle: $errorMessage")
        AlertDialog.Builder(this)
            .setTitle(alertTitle)
            .setMessage(errorMessage)
            .setPositiveButton(
                "ok"
            ) { _, _ -> this.finish() }
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }
}
