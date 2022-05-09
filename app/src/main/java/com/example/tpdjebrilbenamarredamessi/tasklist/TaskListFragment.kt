package com.example.tpdjebrilbenamarredamessi.tasklist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import coil.load
import coil.transform.CircleCropTransformation
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tpdjebrilbenamarredamessi.R
import com.example.tpdjebrilbenamarredamessi.tasklist.dataClassTask.Task
import com.example.tpdjebrilbenamarredamessi.user.UserInfoActivity
import com.example.tpdjebrilbenamarredamessi.databinding.FragmentTaskListBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import form.FormActivity
import com.example.tpdjebrilbenamarredamessi.network.Api
import kotlinx.coroutines.launch
import androidx.fragment.app.viewModels
import java.util.*

class TaskListFragment : Fragment() {
    private val adapter = TaskListAdapter()
    private var taskList = listOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )
    private val viewModel: TasksListViewModel by viewModels()


    val createTask =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = result.data?.getSerializableExtra("task") as? Task
                ?: return@registerForActivityResult
            taskList = taskList + task
            refreshList()
        }
    val updateTask =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = result.data?.getSerializableExtra("task") as? Task
            if (task != null) {
                taskList = taskList.map {
                    if (it.id == task.id) task else it

                }
            }
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

        binding.avatarImageView.setOnClickListener {
            val intent = Intent(context, UserInfoActivity::class.java)
            startActivity(intent)
        }


        adapter.onClickDelete = { task ->
            lifecycleScope.launch {
                viewModel.create(task)
            }
            adapter.submitList(taskList)
        }



        adapter.onClickEdit = { task ->
            val intent = Intent(context, FormActivity::class.java)
            intent.putExtra("task", task)
            updateTask.launch(intent)
            refreshList()
        }
    }

    private fun refreshList() {
        adapter.currentList = taskList
        adapter.notifyDataSetChanged()
    }

//        lifecycleScope.launch {
//            viewModel.tasksStateFlow.collect { newList ->
//                taskList = newList
//                adapter.submitList(taskList)
//            }
//        }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            val userInfo = Api.userWebService.getInfo().body()!!
            val userInfos = binding.textView
           userInfos.text = "${userInfo.firstName} ${userInfo.lastName}"

            binding.avatarImageView.load("https://goo.gl/gEgYUd") {
                crossfade(true)
                transformations(CircleCropTransformation())
            }
            }
        }

    }

}
