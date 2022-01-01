package com.todo.todolistwithfragmentandrecyclerview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView


class TodoAdapter (private val todos:MutableList<Todo>, private val context: Context)
    : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    private val db = DBHelper(
        context,
        null
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view:View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val curTodo = db.getTodos()[position]
        holder.bindItems(curTodo)
    }

    override fun getItemCount(): Int {
        return db.getTodos().size
    }

    fun addTodo(todo: Todo) {
        db.addTodo(todo)
        notifyItemInserted(0)
    }

    private fun toggleStrikeThrough(title:TextView, isChecked:Boolean) {
        if(isChecked) {
            title.paintFlags = title.paintFlags or STRIKE_THRU_TEXT_FLAG
        } else {
            title.paintFlags = title.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val tvTitle:TextView = itemView.findViewById(R.id.tvTodoTitle)
        private val tvDate:TextView = itemView.findViewById(R.id.tvTodoDate)
        private val cbDone:CheckBox = itemView.findViewById(R.id.cbDone)
        private val ivDelete = itemView.findViewById<ImageView>(R.id.ivDelete)


        fun bindItems(todo:Todo) {

            tvTitle.text = todo.title
            tvDate.text = todo.date
            cbDone.isChecked = todo.isChecked

            toggleStrikeThrough(tvTitle, cbDone.isChecked)

            cbDone.setOnClickListener {
                toggleStrikeThrough(tvTitle, cbDone.isChecked)
                db.isChecked(todo.id, tvTitle.text.toString(), tvDate.text.toString(), cbDone.isChecked)
            }

            ivDelete.setOnClickListener {
                deleteTodo(todo.id)
            }

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteTodo(position: Int) {
        db.deleteTodo(position)
        db.updateTableAfterDelete()
        notifyDataSetChanged()
    }

}