package wanchuda.reduks.common.action

import com.beyondeye.reduks.Action
import io.realm.RealmObject
import kotlin.reflect.KClass

interface BaseDbAction<T : RealmObject> {

    val klass: KClass<T>
    val nextAction: (Any) -> Action

}