package by.chapailo.tasksapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TaskDao {
    @Insert
    fun insertAll(vararg tasks: TaskEntity)

    @Delete
    fun deleteTask(task: TaskEntity)

    @Query( "SELECT * FROM tasks " +
            "ORDER BY CASE WHEN priority = 'HIGH' THEN 1 " +
                          "WHEN priority = 'MEDIUM' THEN 2 " +
                          "ELSE 3 END")
    fun loadAll() : List<TaskEntity>

    @Query( "UPDATE tasks SET title =:title, description =:desc " +
            "WHERE id ==:id AND priority ==:priority ")
    fun update(id : Long, title : String, desc : String, priority: String)

    @Query( "SELECT * FROM tasks WHERE priority ==:priority")
    fun loadPriorityAll(priority : String) : List<TaskEntity>

}