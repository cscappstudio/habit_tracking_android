package com.cscmobi.habittrackingandroid.thanhlv.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cscmobi.habittrackingandroid.data.model.HabitCollection
import com.cscmobi.habittrackingandroid.thanhlv.model.Challenge
import com.cscmobi.habittrackingandroid.thanhlv.model.History
import com.cscmobi.habittrackingandroid.thanhlv.model.Mood
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Database(entities = [Task::class, Challenge::class, History::class, HabitCollection::class, Mood::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        private const val DATABASE_NAME = "data.db"

        @Volatile
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .allowMainThreadQueries()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Initialization code on database creation

//                            if(db.isOpen) {
//                                initData(instance)
//                                db.close()
//
//                            }
                        }

                    })
                    .build()
            }
            return instance as AppDatabase
        }

        fun initData(database: AppDatabase?) {


            database?.runInTransaction{

                var tasks = arrayListOf<Task>(
                    Task(0, name = "task 1",""),
                )
                var histories = arrayListOf<History>(
                    History(),
                )


                    database.dao().insertAll(*tasks.toTypedArray())
                    database.dao().insertAllHistory(*histories.toTypedArray())

            }
        }
    }



    abstract fun dao(): Dao
}
