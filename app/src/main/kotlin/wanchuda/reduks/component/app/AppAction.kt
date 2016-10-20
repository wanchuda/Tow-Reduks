package wanchuda.reduks.component.app

import com.beyondeye.reduks.StandardAction

sealed class AppAction(override val payload: Any? = null,
                       override val error: Boolean = false) : StandardAction {

    //global action
    class LogAction(override val payload: String) : AppAction()

}