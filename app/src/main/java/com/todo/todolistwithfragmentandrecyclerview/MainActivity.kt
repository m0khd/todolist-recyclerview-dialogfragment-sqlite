package com.todo.todolistwithfragmentandrecyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getTodoList()

        val fab:View = findViewById(R.id.gotoAddTodoFragment)
        fab.setOnClickListener {

            // show dialog fragment
            AddTodoDialogFragment.newInstance().show(supportFragmentManager, null)

        }

    }

    fun getTodoList() {
        val db = DBHelper(this, null)

        val recyclerView: RecyclerView = findViewById(R.id.rvTodoItems)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val adapter:TodoAdapter =TodoAdapter(db.getTodos(), this)
        recyclerView.adapter = adapter
    }

}