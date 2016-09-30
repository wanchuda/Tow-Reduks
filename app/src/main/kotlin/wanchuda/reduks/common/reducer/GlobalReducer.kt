package wanchuda.reduks.common.reducer

import android.util.Log
import com.beyondeye.reduks.Reducer
import wanchuda.reduks.component.app.AppAction
import wanchuda.reduks.component.app.AppState

fun globalReducer() = Reducer<AppState> { state, action ->
    Log.d("TOW", "--globalReducer: $action")
    when (action) {
        is AppAction -> when (action) {
            is AppAction.LogAction -> {

                state.copy(logState = state.logState.copy(log = action.payload))
            }
        }
        else -> state
    }
}