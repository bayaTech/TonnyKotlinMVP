package com.tonnybaya.tonnykotlinmvp

import android.app.Application
import android.content.Context
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import kotlin.properties.Delegates

/**
 * @author Tonny Baya <Tonnybee@outlook.com>
 */
class MyApplication : Application() {

    private var refWatcher: RefWatcher? = null

    /**
     * This object can access class private methods
     * and properties
     *
     * Similar to static in Java
     */
    companion object {
        private val TAG = "My Application"

        var context: Context by Delegates.notNull()
            private set

        fun getRefWatcher(context: Context): RefWatcher? {
            val myApplication = context.applicationContext as MyApplication
            return myApplication.refWatcher
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        refWatcher = setupLeakCanary()
        initConfig()
    }

    private fun setupLeakCanary(): RefWatcher {
        return if(LeakCanary.isInAnalyzerProcess(this)){
            RefWatcher.DISABLED
        }else LeakCanary.install(this)
    }

    private fun initConfig(){
        val formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)
                .methodCount(0)
                .methodOffset(7)
                .tag("BayaTech")
                .build()

        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy){
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })

    }

}