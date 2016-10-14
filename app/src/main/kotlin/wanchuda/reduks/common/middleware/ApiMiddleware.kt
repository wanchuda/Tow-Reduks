package wanchuda.reduks.common.middleware

import android.util.Log
import com.beyondeye.reduks.Middleware
import com.beyondeye.reduks.MiddlewareFn
import com.github.kittinunf.fuel.httpGet
import wanchuda.reduks.common.action.ApiAction
import wanchuda.reduks.common.api.MainApi
import wanchuda.reduks.common.separator.ApiResponseType
import wanchuda.reduks.common.separator.ApiState
import wanchuda.reduks.component.app.AppState
import wanchuda.reduks.extension.rx_gsonResult
import wanchuda.reduks.model.Post

val apiMiddleware: Middleware<AppState> = MiddlewareFn { store, nextDispatcher, action ->
    Log.d("TOW", "(apiMiddleware) - $action")
    when (action) {
        is ApiAction -> {
            val action = action as ApiAction
            when (action) {
                is ApiAction.RequestApi -> {
                    Log.d("TOW", "//RequestApi: query - ${action.payload}")
                    nextDispatcher.invoke(action.onRequesting(null))

                    // TODO : Call GraphQL later
                    // MainApi.CallGraphQL(payload).httpPost()

                    val fuel = MainApi.GetPost().httpGet()
                            .responseString { request, response, result ->
                                Log.d("TOW", "//RequestApi: request - $request")
                                Log.d("TOW", "//RequestApi: response - $response")
                            }

                    val observable =
                            when (action.responseType) {
                                ApiResponseType.POST_LIST -> fuel.rx_gsonResult<List<Post>>()
                            //TODO : Other type
                                else -> fuel.rx_gsonResult<List<Post>>()
                            }

                    observable.subscribe {
                        it.fold({ value ->
                                    Log.d("TOW", "//RequestApi: status - ${ApiState.SUCCESS}")
                                    nextDispatcher.invoke(action.onSuccess(value))
                                }, { error ->
                                    Log.d("TOW", "//RequestApi: status - ${ApiState.FAIL}")
                                    nextDispatcher.invoke(action.onFail(null))
                                })
                    }
                }
            }
        }
        else -> {

        }
    }
    nextDispatcher.invoke(action)
}