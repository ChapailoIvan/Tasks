package by.chapailo.tasksapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.chapailo.tasksapp.FragmentNavigator
import com.example.notesapplication.R
import by.chapailo.tasksapp.data.model.Priority
import com.example.notesapplication.databinding.FragmentMainBinding
import by.chapailo.tasksapp.utils.adapters.TaskAdapter
import by.chapailo.tasksapp.utils.factory

class MainFragment : Fragment() {

    private val binding: FragmentMainBinding by lazy {
        FragmentMainBinding.inflate(layoutInflater)
    }

    private val navigator: FragmentNavigator
        get() = context as FragmentNavigator

    private val viewModel: MainViewModel by viewModels { factory() }

    private lateinit var adapter: TaskAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)

        adapter = TaskAdapter(
            { viewModel.delete(it) },
            {
                navigator.open(
                    TaskFragment.newInstance(
                        it.id,
                        it.title,
                        it.description,
                        it.priority
                    )
                )
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.tasksView.adapter = adapter

        viewModel.tasksLiveData.observe(viewLifecycleOwner) {
            adapter.dataSet = it
        }

        viewModel.mPriorityLiveData.observe(viewLifecycleOwner) {
            if (it != Priority.EMPTY) {
                with(binding) {
                    addButton.backgroundTintList =
                        ContextCompat.getColorStateList(requireContext(), it.color)
                    titleView.text = getText(R.string.show_all)
                }

                setVisibility(isVisible = true)
            } else {
                binding.titleView.text = getText(R.string.app_name)

                setVisibility(isVisible = false)
            }
        }

        binding.tasksView.layoutManager = LinearLayoutManager(context)

        binding.titleView.setOnClickListener {
            viewModel.setPriority(Priority.EMPTY)
        }

        binding.settingsButton.setOnClickListener { }

        binding.addButton.setOnClickListener {
            navigator.open(TaskFragment.newInstance(viewModel.mPriorityLiveData.value!!))
        }

        with(binding) {
            redButton.setOnClickListener { viewModel.setPriority(Priority.HIGH) }
            yellowButton.setOnClickListener { viewModel.setPriority(Priority.MEDIUM) }
            greenButton.setOnClickListener { viewModel.setPriority(Priority.LOW) }
        }

        return binding.root
    }

    private fun setVisibility(isVisible: Boolean) {
        binding.addButton.visibility = if (isVisible) VISIBLE else GONE
    }
}