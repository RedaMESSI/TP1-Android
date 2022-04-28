package form

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.tpdjebrilbenamarredamessi.R
import com.example.tpdjebrilbenamarredamessi.tasklist.dataClassTask.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class FormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        val task = intent.getSerializableExtra("task") as? Task
        val titleText = findViewById<EditText>(R.id.editTitle)
        titleText.setText(task?.title)
        val descriptionText = findViewById<EditText>(R.id.editdescription)
        descriptionText.setText(task?.description)
        val id = task?.id ?: UUID.randomUUID().toString()


        val submitButton = findViewById<Button>(R.id.button)
        submitButton.setOnClickListener {
            val newTask = Task(
                id = id,
                title = titleText.text.toString(),
                description = descriptionText.text.toString()
            )

            intent.putExtra("task", newTask)
            setResult(RESULT_OK, intent)
            finish()

//        addTask.setOnClickListener {
//            val newTask = Task(id = UUID.randomUUID().toString(), title = "New Task !")
//
//
//            intent.putExtra("task", newTask)
//            setResult(RESULT_OK, intent)
//           finish()
//        }

        }
    }
}