package wanchuda.reduks.common.api

import com.github.kittinunf.fuel.Fuel

sealed class MainApi(val relativePath: String) : Fuel.PathStringConvertible {

    override val path = "/$relativePath"

    class GetPost() : MainApi("posts")

}