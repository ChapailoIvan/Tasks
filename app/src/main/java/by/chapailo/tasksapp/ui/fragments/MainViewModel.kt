package by.chapailo.tasksapp.ui.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.chapailo.tasksapp.data.model.Priority
import by.chapailo.tasksapp.data.model.Task
import by.chapailo.tasksapp.data.repositories.TasksRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: TasksRepository
) : ViewModel() {

    var mPriorityLiveData = MutableLiveData(Priority.EMPTY)

    private val mTasksLiveData : MutableLiveData<List<Task>> = MutableLiveData()
    val tasksLiveData : LiveData<List<Task>> = mTasksLiveData

    init {
        repository.addListener { loadFilteredTasks(mPriorityLiveData.value!!) }

        viewModelScope.launch {
            mTasksLiveData.value = repository.loadAll()
        }
    }

    fun setPriority(priority: Priority) {
        mPriorityLiveData.value = priority
        if (priority == Priority.EMPTY) loadAll() else loadFilteredTasks(priority)
    }

    fun delete(task : Task) {
        viewModelScope.launch {
            repository.delete(task)
        }
    }

    private fun loadFilteredTasks(priority: Priority) {
        viewModelScope.launch {
            mTasksLiveData.value = repository.loadFilteredTasks(priority.name)
        }
    }

    private fun loadAll() {
        viewModelScope.launch {
            mTasksLiveData.value = repository.loadAll()
        }
    }
}