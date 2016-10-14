package wanchuda.reduks.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.beyondeye.reduks.logger.ReduksLogger
import com.beyondeye.reduks.logger.ReduksLoggerConfig
import com.beyondeye.reduks.middlewares.applyMiddleware
import wanchuda.reduks.common.ApiMiddleware
import wanchuda.reduks.component.app.AppState
import wanchuda.reduks.component.app.appStore

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appStore.applyMiddleware(
                ReduksLogger<AppState>(ReduksLoggerConfig(actionTypeExtractor = { any -> any.javaClass.canonicalName }, logActionDuration = true)),
                ApiMiddleware<AppState>()
        )

//        appStore.applyStandardMiddlewares()
    }

}
