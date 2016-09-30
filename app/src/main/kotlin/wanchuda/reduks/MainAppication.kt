package wanchuda.reduks

import android.app.Application
import android.util.Log
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.core.interceptors.cUrlLoggingRequestInterceptor
import io.realm.Realm
import io.realm.RealmConfiguration

class MainApplication : Application() {

    val API_URL = "http://jsonplaceholder.typicode.com"

    lateinit var realmConfig: RealmConfiguration

    override fun onCreate() {
        super.onCreate()
        setUpRealm()
        setUpFuel()
    }

    private fun setUpRealm() {
        Realm.init(this)
        realmConfig = RealmConfiguration.Builder()
                .name("Tow-Reduks.realm")
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(realmConfig)
    }

    private fun setUpFuel() {
        FuelManager.instance.basePath = API_URL
        FuelManager.instance.apply {
            addRequestInterceptor(cUrlLoggingRequestInterceptor())
            addRequestInterceptor { next: (Request) -> Request ->
                { req: Request ->
                    Log.d("TOW", "Request $req")
                    next(req)
                }
            }
            addResponseInterceptor { next: (Request, Response) -> Response ->
                { req: Request, res: Response ->
                    Log.d("TOW", "Response $res")
                    next(req, res)
                }
            }
        }
    }

}