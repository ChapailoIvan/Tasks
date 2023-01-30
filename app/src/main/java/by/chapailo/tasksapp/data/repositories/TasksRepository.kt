package by.chapailo.tasksapp.data.repositories

import by.chapailo.tasksapp.data.database.TaskEntity
import by.chapailo.tasksapp.data.database.TasksDatabase
import by.chapailo.tasksapp.data.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

typealias OnDataHasChangedListener = (List<Task>) -> Unit

class TasksRepository (private var database: TasksDatabase) : Repository {

    private lateinit var onDataHasChangedListener : OnDataHasChangedListener

    suspend fun insert(task : Task) {
        withContext(Dispatchers.IO) {
            database.getTaskDao().insertAll(TaskEntity.fromTask(task))
            notifyAllListeners()
        }
    }

    suspend fun delete(task : Task) {
        withContext(Dispatchers.IO) {
            database.getTaskDao().deleteTask(TaskEntity.fromTask(task))
            notifyAllListeners()
        }
    }

    suspend fun update(task : Task) {
        withContext(Dispatchers.IO) {
            database.getTaskDao().update(task.id, task.title, task.description, task.priority.name)
            notifyAllListeners()
        }
    }

    suspend fun loadAll() = withContext(Dispatchers.IO) {
        return@withContext database.getTaskDao().loadAll().map {
            it.toTask()
        }
    }

    suspend fun loadFilteredTasks(priority: String) = withContext(Dispatchers.IO) {
        return@withContext database.getTaskDao().loadPriorityAll(priority).map {
            it.toTask()
        }
    }

    fun addListener(listener: OnDataHasChangedListener) {
        onDataHasChangedListener = listener
    }

    private suspend fun notifyAllListeners() {
        onDataHasChangedListener.invoke(loadAll())
    }
}