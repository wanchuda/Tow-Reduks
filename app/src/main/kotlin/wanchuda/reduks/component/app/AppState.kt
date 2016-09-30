package wanchuda.reduks.component.app

import wanchuda.reduks.common.state.LogState
import wanchuda.reduks.component.post.PostState

data class AppState(
        var logState: LogState = LogState("init"),
        var testState: PostState = PostState()
                   )
