package wanchuda.reduks.common

import com.beyondeye.reduks.Middleware
import com.beyondeye.reduks.Reducer
import com.beyondeye.reduks.Store
import com.github.kittinunf.fuel.httpGet
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import wanchuda.reduks.common.api.MainApi
import wanchuda.reduks.common.separator.ApiState
import wanchuda.reduks.component.app.AppState
import wanchuda.reduks.model.Post
import java.lang.reflect.Type

sealed class ApiAction {

    class Request(val query: String, val dataClass: Type) : ApiAction()

    class Success(val payload: Any) : ApiAction()

    class Failure(val throwable: Throwable) : ApiAction()
}


inline fun <reified T> Gson.fromJson(json: String): T = fromJson(json, object : TypeToken<T>() {}.type)

class ApiMiddleware<S>() : Middleware<S> {
    override fun dispatch(store: Store<S>, nextDispatcher: (Any) -> Any, action: Any): Any {
        val dispatcher = nextDispatcher(action)
        when (action) {
            is ApiAction -> when (action) {
                is ApiAction.Request -> handleRequest(store, nextDispatcher, action)
                is ApiAction.Success -> handleSuccess(store, nextDispatcher, action)
                is ApiAction.Failure -> handleFailure(store, nextDispatcher, action)
            }
        }
        return dispatcher
    }

    private fun handleRequest(store: Store<S>, nextDispatcher: (Any) -> Any, action: ApiAction.Request): Any {
        MainApi.GetPost().httpGet().responseString { request, response, result ->
            result.fold({
                store.dispatch(ApiAction.Success(AppGson().fromJson(it, action.dataClass)))
            }, {
                store.dispatch(ApiAction.Failure(it))
            })
        }
        return nextDispatcher(action)
    }

    private fun handleSuccess(store: Store<S>, nextDispatcher: (Any) -> Any, action: ApiAction.Success): Any {
        return when (action) {
            is ApiAction.Success -> {
                //TODO dispatch a new DbAction
            }
            else -> {
            }
        }
    }

    private fun handleFailure(store: Store<S>, nextDispatcher: (Any) -> Any, action: ApiAction.Failure): Any {
        return when (action) {
            is ApiAction.Failure -> {
                //TODO not sure what to do here
            }
            else -> {
            }
        }
    }
}

class ApiReducer : Reducer<AppState> {
    override fun reduce(state: AppState, action: Any): AppState = if (action is ApiAction) {
        when (action) {
            is ApiAction.Request -> handleRequest(state, action)
            is ApiAction.Success -> handleSuccess(state, action)
            is ApiAction.Failure -> handleFailure(state, action)
            else -> state
        }
    } else {
        state
    }

    private fun handleRequest(state: AppState, action: ApiAction.Request): AppState {
        return when (action) {
            is ApiAction.Request -> {
                state.copy(testState = state.testState.copy(apiState = ApiState.REQUESTING))
            }
            else -> {
                state
            }
        }
    }

    private fun handleSuccess(state: AppState, action: ApiAction.Success): AppState {
        val payload = action.payload
        return when (payload) {
            is Post -> {
                state.copy(testState = state.testState.copy(apiState = ApiState.SUCCESS, post = action.payload as List<Post>))
            }
            else -> {
                state
            }
        }
    }

    private fun handleFailure(state: AppState, action: ApiAction.Failure): AppState {
        return when (action) {
            is ApiAction.Failure -> {
                state.copy(testState = state.testState.copy(apiState = ApiState.FAIL))
            }
            else -> {
                state
            }
        }
    }
}
