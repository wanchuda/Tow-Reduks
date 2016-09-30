package wanchuda.reduks.common.middleware

import android.util.Log
import com.beyondeye.reduks.Middleware
import wanchuda.reduks.component.app.AppState

val loggingMiddleware: Middleware<AppState> = Middleware { appStore, nextDispatcher, action ->
    Log.d("TOW", "(loggingMiddleware) - $action")
    nextDispatcher.dispatch(action)
}