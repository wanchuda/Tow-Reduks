package wanchuda.reduks.common.middleware

import android.util.Log
import com.beyondeye.reduks.Middleware
import com.beyondeye.reduks.MiddlewareFn
import wanchuda.reduks.component.app.AppState

val loggingMiddleware: Middleware<AppState> = MiddlewareFn { appStore, nextDispatcher, action ->
    Log.d("TOW", "(loggingMiddleware) - $action")
    nextDispatcher.invoke(action)
}