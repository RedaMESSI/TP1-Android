package com.example.tpdjebrilbenamarredamessi.tasklist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tpdjebrilbenamarredamessi.R
import com.example.tpdjebrilbenamarredamessi.tasklist.dataClassTask.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import form.FormActivity
import java.util.*

class TaskListFragment : Fragment() {
    private val adapter = TaskListAdapter()
    private var taskList = listOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )
    val createTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as? Task
            ?: return@registerForActivityResult
        taskList = taskList + task
        refreshList()
    }

    val modifyTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
    val intent = intent(context, FormActivity::class.java)
        intent.putExtra("task", task)
        modifyTask.launch(intent)
        refreshList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_blank, container, false)
        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter.currentList = taskList;
        recyclerView.adapter = adapter

        val addButton = view.findViewById<FloatingActionButton>(R.id.floatingActionButton)
        addButton.setOnClickListener {

            val intent = Intent(context, FormActivity::class.java)
            createTask.launch(intent)
        }


        adapter.onClickDelete = { task ->
            taskList = taskList - task
            refreshList()
        }
    }

    private fun refreshList() {
        adapter.currentList = taskList
        adapter.notifyDataSetChanged()
    }
}
