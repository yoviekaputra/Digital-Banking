package mlpt.siemo.digitalbanking.module.fingerprint

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.activity_finger_print.*
import mlpt.siemo.digitalbanking.R
import java.util.concurrent.Executors

class FingerPrintActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finger_print)

        val executor = Executors.newSingleThreadExecutor()
        val activity: FragmentActivity = this // reference to activity
        val biometricPrompt = BiometricPrompt(activity, executor, object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    // user clicked negative button
                } else {
                    toast("Called when an unrecoverable error has been encountered and the operation is complete.")
                }
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Log.d("finger", "Auth success $result")
                toast("Auth success " + result.cryptoObject)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                toast("failed")
            }
        })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Verification")
            .setSubtitle("You will enter to my app")
            .setDescription("Put your finger on device")
            .setNegativeButtonText("Close")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    fun toast(msg: String?) {
        runOnUiThread {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }
}
