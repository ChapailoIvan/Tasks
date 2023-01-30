package by.chapailo.tasksapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.example.notesapplication.R
import by.chapailo.tasksapp.data.database.TasksDatabase
import by.chapailo.tasksapp.data.repositories.TasksRepository
import by.chapailo.tasksapp.ui.fragments.MainFragment

class MainActivity : AppCompatActivity(), FragmentNavigator, RepositoryProvider {

    lateinit var tasksRepository: TasksRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tasksRepository = TasksRepository(
                database = Room.databaseBuilder(
                    applicationContext, TasksDatabase::class.java,
                    "tasks-database.db")
                    .allowMainThreadQueries().build()
        )

       if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container_view, MainFragment())
            .commit()
    }

    override fun open(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(fragment.javaClass.simpleName)
            .replace(R.id.fragment_container_view, fragment)
            .commit()
    }

    override fun overlay(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(fragment.javaClass.simpleName)
            .add(R.id.fragment_container_view, fragment)
            .commit()
    }

    override fun startDialog() {

    }

    override fun close() = supportFragmentManager.popBackStack()

    override fun provide() = tasksRepository
}

interface FragmentNavigator {

    fun open(fragment: Fragment)

    fun overlay(fragment: Fragment)

    fun startDialog()

    fun close()

    class Empty : FragmentNavigator {
        override fun open(fragment: Fragment) = Unit
        override fun close() = Unit
        override fun overlay(fragment: Fragment) = Unit
        override fun startDialog() = Unit
    }
}

interface RepositoryProvider {
    fun provide() : TasksRepository
}


