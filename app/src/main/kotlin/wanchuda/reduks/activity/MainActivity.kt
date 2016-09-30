package wanchuda.reduks.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.github.kittinunf.reactiveandroid.support.v7.widget.rx_itemsWith
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_main.btNext
import kotlinx.android.synthetic.main.activity_main.btTest
import kotlinx.android.synthetic.main.activity_main.rvTest
import kotlinx.android.synthetic.main.item_test.view.tvTitle
import wanchuda.reduks.R
import wanchuda.reduks.component.app.appStore
import wanchuda.reduks.component.post.PostAction
import wanchuda.reduks.model.Post

class MainActivity : BaseActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpUI()
        setUpRealm()
        appStore.stateChanges.subscribe {
            Log.d("TOW", "**** Log ${appStore.state.logState.log}")
            Log.d("TOW", "**** Data ${appStore.state.testState.post.size}")
            Log.d("TOW", "**** Api ${appStore.state.testState.apiState.name}")
            Log.d("TOW", "**** DB ${appStore.state.testState.databaseState.name}")
        }
    }

    fun setUpUI() {
        btTest.setOnClickListener { appStore.dispatch(PostAction.FetchPostList()) }
        btNext.setOnClickListener { startActivity(Intent(this@MainActivity, NextActivity::class.java)) }
        rvTest.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            rx_itemsWith(
                    observable = appStore.stateChanges.map { it.testState.post }.asObservable(),
                    onCreateViewHolder = { parent, viewType ->
                        val resId = R.layout.item_test
                        val view = LayoutInflater.from(parent?.context).inflate(resId, parent, false)
                        VH(view)
                    },
                    onBindViewHolder = { holder, position, item ->
                        holder.itemView.apply { tvTitle.text = item.title }
                    })
        }
    }

    fun setUpRealm() {


        val realm = Realm.getDefaultInstance()
        // First init data in database
        // For check query data from database should show before get data from api
        realm.beginTransaction()
        realm.copyToRealmOrUpdate(Post(id = "0", title = "ABC"))
        realm.copyToRealmOrUpdate(Post(id = "1", title = "DEF"))
        realm.commitTransaction()
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {}

}