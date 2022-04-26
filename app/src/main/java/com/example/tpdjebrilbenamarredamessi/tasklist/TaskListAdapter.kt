package com.example.tpdjebrilbenamarredamessi.tasklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tpdjebrilbenamarredamessi.R
import com.example.tpdjebrilbenamarredamessi.tasklist.dataClassTask.Task

class TaskListAdapter : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {
    var currentList: List<Task> = emptyList()

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView = itemView.findViewById<TextView>(R.id.task_title);
        val descriptionTextView= itemView.findViewById<TextView>(R.id.task_description);
        fun bind(task: Task) {
            textView.text = task.title
            descriptionTextView.text = task.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun getItemCount(): Int {

        return currentList.size
    }

}
