package wanchuda.reduks.common.middleware

import android.util.Log
import com.beyondeye.reduks.Middleware
import com.beyondeye.reduks.MiddlewareFn
import io.realm.Realm
import wanchuda.reduks.common.action.DbAction
import wanchuda.reduks.common.separator.ApiState
import wanchuda.reduks.common.separator.DbState
import wanchuda.reduks.component.app.AppState
import wanchuda.reduks.component.post.PostAction
import wanchuda.reduks.model.Post

val databaseMiddleware: Middleware<AppState> = MiddlewareFn { store, nextDispatcher, action ->
    Log.d("TOW", "(databaseMiddleware) - $action")
    if (action is DbAction) {
        val realm = Realm.getDefaultInstance()
        when (action) {

        //================================================================================
        // region Action
        //================================================================================

            is DbAction.QueryPostListDb -> {
                Log.d("TOW", "//QueryProjectListDb")
                nextDispatcher.invoke(PostAction.UpdatePostList(
                        payload = realm.where(Post::class.java).findAll().toList(),
                        apiState = ApiState.UNCHANGED,
                        dbState = DbState.SUCCESS))
            }
            is DbAction.UpdatePostListDb -> {
                Log.d("TOW", "//UpdateProjectListDb")
                realm.beginTransaction()
                action.payload.forEach {
                    realm.copyToRealmOrUpdate(it)
                }
                realm.commitTransaction()
                nextDispatcher.invoke(PostAction.UpdatePostList(
                        payload = null,
                        apiState = ApiState.UNCHANGED,
                        dbState = DbState.SAVE))
            }

        //endregion

        //================================================================================
        // region Generic
        //================================================================================

            is DbAction.QueryList<*> -> {
                Log.d("TOW", "//QueryList")
                val payload = realm.where(action.klass.java).findAll().toList()
                nextDispatcher.invoke(action.nextAction(payload))
            }

            is DbAction.UpdateList<*> -> {
                Log.d("TOW", "//UpdateList")
                realm.beginTransaction()
                action.payload.forEach {
                    realm.copyToRealmOrUpdate(it)
                }
                realm.commitTransaction()
                nextDispatcher.invoke(action.nextAction)
            }

        //endregion
        }
        realm.close()
    } else {
        nextDispatcher.invoke(action)
    }
}