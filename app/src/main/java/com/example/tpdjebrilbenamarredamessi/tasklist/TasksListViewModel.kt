package com.example.tpdjebrilbenamarredamessi.tasklist


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpdjebrilbenamarredamessi.network.Api
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.internal.concurrent.Task

class TasksListViewModel : ViewModel() {
    private val webService = Api.userTaskWebService


    private val _tasksStateFlow = MutableStateFlow<List<Task>>(emptyList())
    public val tasksStateFlow: StateFlow<List<Task>> = _tasksStateFlow.asStateFlow()

    suspend fun refresh() {
        viewModelScope.launch {
            val response = webService.getTasks()
            if (response.isSuccessful) {
                val fetchedTasks = response.body()!!
                _tasksStateFlow.value = fetchedTasks

            }

        }
    }

    suspend fun update(task: Task) {
        viewModelScope.launch {
            val response = webService.update(task, task.name)
            if (!response.isSuccessful) {
                val updatedTask = response.body()!!
                _tasksStateFlow.value = _tasksStateFlow.value - task + updatedTask
            }
        }

    }

    suspend fun create(task: Task) {
        viewModelScope.launch {
            val response = webService.create(task)
            if (!response.isSuccessful) {
                val newTask = response.body()!!
                _tasksStateFlow.value = _tasksStateFlow.value + newTask
            }
        }

    }


    suspend fun delete(task: Task) {
        viewModelScope.launch {
            val response = webService.delete(task.name)
            if (!response.isSuccessful) {
                val newTask = response.body()!!
                _tasksStateFlow.value = _tasksStateFlow.value - task
            }


        }
    }
}