package wanchuda.reduks.component.global

import android.util.Log
import com.beyondeye.reduks.ReducerFn
import wanchuda.reduks.component.app.AppAction
import wanchuda.reduks.component.app.AppState

fun globalReducer() = ReducerFn<AppState> { state, action ->
    Log.d("TOW", "--globalReducer: $action")
    when (action) {
        is AppAction -> {
            val action = action as AppAction
            when (action) {
                is AppAction.LogAction -> {
                    state.copy(logState = state.logState.copy(log = action.payload))
                }
            }
        }
        else -> state
    }
}