package wanchuda.reduks.common.action

import com.beyondeye.reduks.Action

sealed class ApiAction(open val type: String) : Action {

    class Request(type: String, val query: String) : ApiAction(type)
    class Success(type: String, val response: Any) : ApiAction(type)
    class Failure(type: String, val t: Throwable) : ApiAction(type)

}