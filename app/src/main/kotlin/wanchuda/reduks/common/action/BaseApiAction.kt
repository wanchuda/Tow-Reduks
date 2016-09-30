package wanchuda.reduks.common.action

import com.beyondeye.reduks.Action
import wanchuda.reduks.common.separator.ApiResponseType

interface BaseApiAction {

    val onRequesting: (Any) -> Action
    val onSuccess: (Any) -> Action
    val onFail: (Any) -> Action
    val responseType: ApiResponseType

}