package by.chapailo.tasksapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.chapailo.tasksapp.FragmentNavigator
import by.chapailo.tasksapp.RepositoryProvider
import by.chapailo.tasksapp.data.model.Priority
import by.chapailo.tasksapp.data.model.Task
import by.chapailo.tasksapp.data.repositories.TasksRepository
import com.example.notesapplication.databinding.FragmentAddTaskBinding
import kotlinx.coroutines.launch

// arguments?.getString("title") != null
// means, that current task was created earlier and contains
// info about title and description, that's why
// we are checking that fields

class TaskFragment : Fragment() {

    private val binding : FragmentAddTaskBinding by lazy {
        FragmentAddTaskBinding.inflate(layoutInflater)
    }

    private val navigator: FragmentNavigator
        get() = context as FragmentNavigator

    private val repository: TasksRepository
        get() = (context as RepositoryProvider).provide()

    private var priority : Priority = Priority.EMPTY
        set(value) {
            field = value
            binding.checkButton.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), field.color)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        priority = Priority.valueOf(arguments?.getString("priority").toString())

        if (arguments?.getString("title") != null) {
            binding.titleEdittext.setText(arguments?.getString("title").toString())
            binding.descEdittext.setText(arguments?.getString("description").toString())
        }

        binding.checkButton.setOnClickListener {
            lifecycleScope.launch {
                val task = Task(
                    id = arguments?.getLong("id") ?: 0,
                    title = binding.titleEdittext.text.toString().ifBlank { "No Title" },
                    description = binding.descEdittext.text.toString().ifBlank { "No Description" },
                    priority = this@TaskFragment.priority
                )

                if (arguments?.getString("title") == null) repository.insert(task) else repository.update(
                    task
                )

                navigator.close()
            }
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(id : Long = 0, title : String, desc : String, priority : Priority) = TaskFragment().apply {
            arguments = Bundle().apply {
                putLong(ID_KEY, id)
                putString(TITLE_KEY, title)
                putString(DESC_KEY, desc)
                putString(PRIORITY_KEY, priority.name)
            }
        }

        @JvmStatic
        fun newInstance(priority: Priority) = TaskFragment().apply {
            arguments = Bundle().apply {
                putString(PRIORITY_KEY, priority.name)
            }
        }

        private const val ID_KEY = "id"
        private const val TITLE_KEY = "title"
        private const val DESC_KEY = "description"
        private const val PRIORITY_KEY = "priority"
    }

}