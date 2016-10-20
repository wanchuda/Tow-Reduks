package wanchuda.reduks.component.app

import com.beyondeye.reduks.middlewares.applyMiddleware
import com.beyondeye.reduks.middlewares.applyStandardMiddlewares
import com.beyondeye.reduks.rx.RxStore
import wanchuda.reduks.common.middleware.apiMiddleware
import wanchuda.reduks.common.middleware.databaseMiddleware
import wanchuda.reduks.common.middleware.loggingMiddleware

val appStore = RxStore(AppState(), appReducer).apply {
    applyMiddleware(
            loggingMiddleware,
            apiMiddleware,
            databaseMiddleware
                   )
    applyStandardMiddlewares()
}