package by.chapailo.tasksapp.data.model

import com.example.notesapplication.R

enum class Priority(val color: Int) {
    HIGH(R.color.red),
    MEDIUM(R.color.yellow),
    LOW(R.color.green),
    EMPTY(R.color.white)
}

data class Task (
    val id : Long,
    val title : String,
    val description : String,
    val priority: Priority
) {

    override fun toString(): String {
        return "[id: $id; title: $title; description: $description]"
    }

}