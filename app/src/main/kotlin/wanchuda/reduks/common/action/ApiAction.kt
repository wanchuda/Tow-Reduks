package wanchuda.reduks.common.action

import com.beyondeye.reduks.Action

sealed class ApiAction : Action {

    class Request<out T>(val query: String, val parse: (String) -> T) : ApiAction()
    class Success(val query: String, val response: Any) : ApiAction()
    class Failure(val query: String, val t: Throwable) : ApiAction()

}