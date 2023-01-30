package by.chapailo.tasksapp.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import by.chapailo.tasksapp.data.model.Priority
import by.chapailo.tasksapp.data.model.Task

@Entity(tableName = "tasks")
data class TaskEntity(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id : Long,
    @ColumnInfo(name = "title") val title : String,
    @ColumnInfo(name = "description") val description : String,
    @ColumnInfo(name = "priority") val priority: String
) {
    fun toTask() = Task(id, title, description, Priority.valueOf(priority))

    companion object {
        fun fromTask(task : Task) = TaskEntity(task.id, task.title, task.description, task.priority.name)
    }

}