package wanchuda.reduks.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class Post(

        open var userId: String = "",

        @PrimaryKey
        open var id: String = "",

        open var title: String = "",

        open var body: String = ""

               ) : RealmObject()
