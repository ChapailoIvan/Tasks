package by.chapailo.tasksapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TaskEntity::class], version = 1)
abstract class TasksDatabase : RoomDatabase() {

    abstract fun getTaskDao() : TaskDao

}