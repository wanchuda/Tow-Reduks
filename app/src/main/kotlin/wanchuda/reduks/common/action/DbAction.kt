package wanchuda.reduks.common.action

import com.beyondeye.reduks.Action
import com.beyondeye.reduks.StandardAction
import io.realm.RealmObject
import wanchuda.reduks.model.Post
import kotlin.reflect.KClass

sealed class DbAction(override val payload: Any? = null,
                      override val error: Boolean = false) : StandardAction {

    //================================================================================
    // region Action
    //================================================================================

    class QueryPostListDb() : DbAction()

    class UpdatePostListDb(override val payload: List<Post>) : DbAction()

    //endregion

    //================================================================================
    // region Generic
    //================================================================================

    class QueryList<T : RealmObject>(override val klass: KClass<T>,
                                     override val nextAction: (Any) -> Action) : DbAction(), BaseDbAction<T>

    class UpdateList<T : RealmObject>(override val payload: List<T>,
                                      override val klass: KClass<T>,
                                      override val nextAction: (Any) -> Action) : DbAction(), BaseDbAction<T>

    //endregion

}