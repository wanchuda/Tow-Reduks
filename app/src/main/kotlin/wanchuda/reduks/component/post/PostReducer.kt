package wanchuda.reduks.component.post

import android.util.Log
import com.beyondeye.reduks.Reducer
import wanchuda.reduks.common.separator.ApiState
import wanchuda.reduks.common.separator.DbState
import wanchuda.reduks.component.app.AppState

fun postReducer(): Reducer<AppState> = Reducer { state, action ->
    Log.d("TOW", "--postReducer: $action")
    when (action) {
        is PostAction -> {
            when (action) {
            //================================================================================
            // region action general
            //================================================================================
                is PostAction.FetchPostList -> {
                    state
                }
                is PostAction.UpdatePostList -> {
                    var apiState = if (action.apiState == ApiState.UNCHANGED) state.testState.apiState else action.apiState
                    var dbState = if (action.dbState == DbState.UNCHANGED) state.testState.databaseState else action.dbState
                    action.payload?.let {
                        state.copy(testState = state.testState.copy(post = action.payload,
                                                                    apiState = apiState,
                                                                    databaseState = dbState))
                    } ?: state.copy(testState = state.testState.copy(apiState = apiState,
                                                                     databaseState = dbState))
                }
            //endregion

            //================================================================================
            // region action from database
            //================================================================================
                is PostAction.QueryPostListFromDb -> {
                    action.payload?.let {
                        state.copy(testState = state.testState.copy(post = action.payload,
                                                                    databaseState = DbState.SUCCESS))
                    } ?: state.copy(testState = state.testState.copy(databaseState = DbState.FAIL))
                }
                is PostAction.SavePostListToDb -> {
                    state.copy(testState = state.testState.copy(databaseState = DbState.SAVE))
                }

            //endregion

            //================================================================================
            // region action from api
            //================================================================================
                is PostAction.FetchPostListApiRequesting -> {
                    state.copy(testState = state.testState.copy(apiState = ApiState.REQUESTING))
                }
                is PostAction.FetchPostListApiSuccess -> {
                    action.payload?.let {
                        state.copy(testState = state.testState.copy(post = action.payload,
                                                                    apiState = ApiState.SUCCESS))
                    } ?: state.copy(testState = state.testState.copy(apiState = ApiState.SUCCESS))

                }
                is PostAction.FetchPostListApiFail -> {
                    state.copy(testState = state.testState.copy(apiState = ApiState.FAIL))
                }
            //endregion

            }
        }
        else -> state
    }
}