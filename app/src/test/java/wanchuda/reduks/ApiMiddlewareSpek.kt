package wanchuda.reduks

import com.beyondeye.reduks.NextDispatcher
import com.beyondeye.reduks.StandardAction
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import wanchuda.reduks.common.middleware.apiMiddleware
import wanchuda.reduks.component.app.appStore

/**
 * Created by travis on 10/5/2016 AD.
 */
@RunWith(JUnitPlatform::class)
class ApiMiddlewareSpek : Spek({
    describe("ApiMiddleware", {
        val store = appStore
        val apiMw = apiMiddleware
        val mockNextDispatcher = mock<NextDispatcher>()
        val stdAction = object : StandardAction {
            override val error: Boolean
                get() = false
            override val payload: Any?
                get() = null
        }

        it("should pass the same action to the next dispatcher StandardAction", {
            apiMw.dispatch(store, mockNextDispatcher, stdAction)
            verify(mockNextDispatcher).dispatch(stdAction)
        })

    })
})