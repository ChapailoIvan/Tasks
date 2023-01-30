package by.chapailo.tasksapp.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.chapailo.tasksapp.RepositoryProvider
import by.chapailo.tasksapp.ui.fragments.MainViewModel

class ViewModelFactory(
    private val repositoryProvider: RepositoryProvider
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when(modelClass) {
            MainViewModel::class.java -> {
                MainViewModel(repository = repositoryProvider.provide())
            }
            else -> {
                throw IllegalAccessException("Unknown ViewModel Class")
            }
        }
        return viewModel as T
    }
}

fun Fragment.factory() = ViewModelFactory(activity as RepositoryProvider)
