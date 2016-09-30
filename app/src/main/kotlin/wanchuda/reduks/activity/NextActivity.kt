package wanchuda.reduks.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import com.github.kittinunf.reactiveandroid.support.v7.widget.rx_itemsWith
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.rvTest
import kotlinx.android.synthetic.main.item_test.view.tvTitle
import rx.Observable
import wanchuda.reduks.R
import wanchuda.reduks.model.Post

class NextActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next)
        setUpRealm()
    }

    fun setUpRealm() {
        val realm = Realm.getDefaultInstance()
        val result = realm.where(Post::class.java).findAll().toList()
        rvTest.apply {
            layoutManager = LinearLayoutManager(this@NextActivity, LinearLayoutManager.VERTICAL, false)
            rx_itemsWith(
                    observable = Observable.just(result),
                    onCreateViewHolder = { parent, viewType ->
                        val resId = R.layout.item_test
                        val view = LayoutInflater.from(parent?.context).inflate(resId, parent, false)
                        VH(view)
                    },
                    onBindViewHolder = { holder, position, item ->
                        holder.itemView.apply {
                            tvTitle.text = item.title
                        }
                    })
        }
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {}

}
