package wanchuda.reduks.common.middleware

import com.beyondeye.reduks.Middleware
import com.beyondeye.reduks.Store
import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import wanchuda.reduks.common.AppGson
import wanchuda.reduks.common.action.ApiAction
import wanchuda.reduks.common.api.MainApi
import wanchuda.reduks.component.app.AppState
import wanchuda.reduks.model.Post

/*
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
}*/

val apiMiddleware = ApiMiddleware<AppState>(object : ApiMiddleware.ResponseParser {
    override fun parse(json: String): Any {
        return AppGson().fromJson(json) ?: Any()
    }
})

inline fun <reified T> Gson.fromJson(json: String): T? = fromJson(json, object : TypeToken<T>() {}.type)

class ApiMiddleware<S>(var responseParser: ResponseParser) : Middleware<S> {
    override fun dispatch(store: Store<S>, nextDispatcher: (Any) -> Any, action: Any): Any {
        //This is where Kraph should be
        when (action) {
            is ApiAction.Request<*> ->
                Fuel.get(MainApi.GetPost().path)
                        .responseString { request, response, result ->
                            result.fold({
                                store.dispatch(ApiAction.Success(action.query, action.parse.invoke(it)!!))
                            }, {
                                store.dispatch(ApiAction.Failure(action.query, it))
                            })
                        }
            is ApiAction.Success -> {
                when (action.response) {
                    is Post -> {
                    }
                    is String -> {
                    }
                }
            }
            is ApiAction.Failure -> {
            }

        }
        return nextDispatcher(action)
    }

    interface ResponseParser {
        fun parse(json: String): Any
    }
}