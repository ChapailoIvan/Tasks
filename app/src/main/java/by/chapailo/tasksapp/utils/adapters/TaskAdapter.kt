package by.chapailo.tasksapp.utils.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import by.chapailo.tasksapp.data.model.Task
import com.example.notesapplication.databinding.ItemTaskBinding

typealias onCompleteTaskListener = (Task) -> Unit
typealias onTaskClickListener = (Task) -> Unit

private class TasksDiffCallback(
    private val oldList : List<Task>,
    private val newList : List<Task>,
) : DiffUtil.Callback(){
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}

class TaskAdapter(
    private var onCompleteTaskListener: onCompleteTaskListener,
    private var onClickListener: onTaskClickListener
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(), View.OnClickListener, View.OnLongClickListener {

    var dataSet : List<Task> = emptyList()
        set(value) {
            val diffCallback = TasksDiffCallback(field, value)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    class TaskViewHolder(
        val binding: ItemTaskBinding,
        adapter: TaskAdapter
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            with(binding.taskTitle) {
                setOnClickListener(adapter)
                setOnLongClickListener(adapter)
            }

            with(binding.taskDescription) {
                setOnClickListener(adapter)
                setOnLongClickListener(adapter)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder =
        TaskViewHolder(
            ItemTaskBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false), this
        )

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        with(holder.binding) {
            taskTitle.text = dataSet[position].title
            taskTitle.tag = dataSet[position]

            taskDescription.text = dataSet[position].description
            taskDescription.tag = dataSet[position]

            icPriority.backgroundTintList =
                ContextCompat.getColorStateList(holder.binding.root.context, dataSet[position].priority.color)
        }
    }

    override fun getItemCount() = dataSet.size

    override fun onClick(v: View?) {
        val task = v?.tag as Task
        onClickListener.invoke(task)
    }

    override fun onLongClick(v: View?): Boolean {
        val task = v?.tag as Task
        onCompleteTaskListener.invoke(task)
        return true
    }

}