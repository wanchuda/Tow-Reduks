package wanchuda.reduks.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.beyondeye.reduks.middlewares.applyMiddleware
import com.beyondeye.reduks.middlewares.applyStandardMiddlewares
import wanchuda.reduks.common.middleware.apiMiddleware
import wanchuda.reduks.common.middleware.databaseMiddleware
import wanchuda.reduks.common.middleware.loggingMiddleware
import wanchuda.reduks.component.app.appStore

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appStore.applyMiddleware(
                loggingMiddleware,
                apiMiddleware,
                databaseMiddleware)

        appStore.applyStandardMiddlewares()
    }

}
