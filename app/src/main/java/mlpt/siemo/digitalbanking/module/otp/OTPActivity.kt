package mlpt.siemo.digitalbanking.module.otp

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_otp.*
import kotlinx.android.synthetic.main.dialog_payment_success.*
import mlpt.siemo.digitalbanking.R
import mlpt.siemo.digitalbanking.utils.DialogUtils

class OTPActivity : AppCompatActivity() {

    private var inputTextArr =  arrayListOf<TextInputEditText>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        inputTextArr.add(otp_1)
        inputTextArr.add(otp_2)
        inputTextArr.add(otp_3)
        inputTextArr.add(otp_4)
        inputTextArr.add(otp_5)
        inputTextArr.add(otp_6)

        for (i in 0 until inputTextArr.size) {
            inputTextArr[i].addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val text = inputTextArr[i].text.toString()
                    if (text == "") {
                        setFocus(i-1)
                    } else {
                        setFocus(i+1)
                    }

                    if (i == inputTextArr.size - 1) {
                        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
                    }
                }
            })
        }

        bt_verify.setOnClickListener {
            pb_loading.visibility = View.VISIBLE
            bt_verify.visibility = View.GONE

            Handler().postDelayed({
                val dialog = DialogUtils.create(this, R.layout.dialog_payment_success)
                dialog.apply {
                    fab.setOnClickListener {
                        dismiss()
                        finish()

                        this@OTPActivity.finish()
                    }
                    show()
                }
            }, 1000)
        }
    }

    private fun setFocus(pos: Int) {
        if (pos > -1 && pos < inputTextArr.size)
            inputTextArr[pos].requestFocus()
    }
}
