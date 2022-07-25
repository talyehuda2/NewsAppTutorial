package com.androiddevs.mvvmnewsapp.db

import android.content.Context
import androidx.room.*
import com.androiddevs.mvvmnewsapp.models.Article

@Database(
    entities = [Article::class],
    version = 1
)

// tell DataBase class about the convertors (Source to String and vice versa)
@TypeConverters(Converters::class)

abstract class ArticleDataBase : RoomDatabase() {

    abstract fun getArticleDao(): ArticleDao

    companion object {
        @Volatile // means that other threads can see when a thread change this instance
        private var instance: ArticleDataBase? = null
        private val LOCK = Any() //use that to synchronize setting up this instance to make sure there only one instance at once

        // this function will be called when ArticleDataBase object been initialized
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            // everything in this block can be accessed only by one thread at a time
            instance ?: createDataBase(context).also { instance = it }
        }

        private fun createDataBase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDataBase::class.java,
                "article_db.db"
            ).build()
    }
}