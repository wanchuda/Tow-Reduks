package wanchuda.reduks.common.action

import com.beyondeye.reduks.Action
import com.beyondeye.reduks.StandardAction
import wanchuda.reduks.common.separator.ApiResponseType

sealed class ApiAction(override val payload: Any? = null,
                       override val error: Boolean = false) : StandardAction {

    class Request(override val payload: Any?,
                  override val onRequesting: (Any?) -> Action,
                  override val onSuccess: (Any) -> Action,
                  override val onFail: (Any?) -> Action,
                  override val responseType: ApiResponseType) : ApiAction(), BaseApiAction

}