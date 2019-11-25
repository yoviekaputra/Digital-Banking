package mlpt.siemo.digitalbanking

import android.app.Application
import android.content.Context
import mlpt.siemo.digitalbanking.utils.LocaleManager

class AppContext : Application() {
    override fun onCreate() {
        super.onCreate()
    }
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleManager.setLocale(base))
    }
}