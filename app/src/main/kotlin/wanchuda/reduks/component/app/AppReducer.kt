package wanchuda.reduks.component.app

import com.beyondeye.reduks.combineReducers
import wanchuda.reduks.component.global.globalReducer
import wanchuda.reduks.component.post.postReducer

val appReducer = combineReducers(
        globalReducer(),
        postReducer()
                                )