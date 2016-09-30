package wanchuda.reduks.component.app

import com.beyondeye.reduks.rx.RxStore

val appStore = RxStore(AppState(), appReducer)