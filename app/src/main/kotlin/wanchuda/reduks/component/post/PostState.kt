package wanchuda.reduks.component.post

import wanchuda.reduks.common.separator.ApiState
import wanchuda.reduks.common.separator.DbState
import wanchuda.reduks.model.Post

data class PostState(
        val post: List<Post> = listOf(),
        val apiState: ApiState = ApiState.NONE,
        val databaseState: DbState = DbState.NONE)