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

sealed class ApiAction {

    sealed class Request(val query: String) : ApiAction() {
        class PostList(query: String) : Request(query = query)
    }

    sealed class Success<T>(val payload: T) : ApiAction() {
        class PostList(postList: List<Post>) : Success<List<Post>>(payload = postList)
    }

    sealed class Failure(val throwable: Throwable) : ApiAction() {
        class PostList(throwable: Throwable) : Failure(throwable = throwable)
    }
}


inline fun <reified T> Gson.fromJson(json: String): T = fromJson(json, object : TypeToken<T>() {}.type)

class ApiMiddleware<S>() : Middleware<S> {
    override fun dispatch(store: Store<S>, nextDispatcher: (Any) -> Any, action: Any): Any {
        val dispatcher = nextDispatcher(action)
        when (action) {
            is ApiAction -> when (action) {
                is ApiAction.Request -> handleRequest(store, nextDispatcher, action)
                is ApiAction.Success<*> -> handleSuccess(store, nextDispatcher, action)
                is ApiAction.Failure -> handleFailure(store, nextDispatcher, action)
            }
        }
        return dispatcher
    }

    private fun handleRequest(store: Store<S>, nextDispatcher: (Any) -> Any, action: ApiAction.Request): Any {
        return when (action) {
            is ApiAction.Request.PostList -> {
                MainApi.GetPost().httpGet().responseString { request, response, result ->
                    result.fold({
                        val p: List<Post> = AppGson().fromJson(it)
                        store.dispatch(ApiAction.Success.PostList(p.subList(0, 30)))
                    }, {
                        store.dispatch(ApiAction.Failure.PostList(it))
                    })
                }
                nextDispatcher(action)
            }
        }
    }

    private fun handleSuccess(store: Store<S>, nextDispatcher: (Any) -> Any, action: ApiAction.Success<*>): Any {
        return when (action) {
            is ApiAction.Success.PostList -> {
                //TODO dispatch a new DbAction
            }
        }
    }

    private fun handleFailure(store: Store<S>, nextDispatcher: (Any) -> Any, action: ApiAction.Failure): Any {
        return when (action) {
            is ApiAction.Failure.PostList -> {
                //TODO not sure what to do here
            }
        }
    }
}

class ApiReducer : Reducer<AppState> {
    override fun reduce(state: AppState, action: Any): AppState = if (action is ApiAction) {
        when (action) {
            is ApiAction.Request -> handleRequest(state, action)
            is ApiAction.Success<*> -> handleSuccess(state, action)
            is ApiAction.Failure -> handleFailure(state, action)
            else -> state
        }
    } else {
        state
    }

    private fun handleRequest(state: AppState, action: ApiAction.Request): AppState {
        return when (action) {
            is ApiAction.Request.PostList -> {
                state.copy(testState = state.testState.copy(apiState = ApiState.REQUESTING))
            }
        }
    }

    private fun handleSuccess(state: AppState, action: ApiAction.Success<*>): AppState {
        return when (action) {
            is ApiAction.Success.PostList -> {
                state.copy(testState = state.testState.copy(apiState = ApiState.SUCCESS, post = action.payload))
            }
        }
    }

    private fun handleFailure(state: AppState, action: ApiAction.Failure): AppState {
        return when (action) {
            is ApiAction.Failure.PostList -> {
                state.copy(testState = state.testState.copy(apiState = ApiState.FAIL))
            }
        }
    }
}
