package com.todo.todolistwithfragmentandrecyclerview

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?)
    : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION){

    override fun onCreate(db: SQLiteDatabase?) {
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                TODO_TITLE + " TEXT," +
                DATE + " TEXT," +
                IS_CHECKED + " INTEGER" + ")")

        db?.execSQL(query)

    }



    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addTodo(todo:Todo) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(TODO_TITLE, todo.title)
        values.put(DATE, todo.date)
        values.put(IS_CHECKED, todo.isChecked)
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getTodos() : MutableList<Todo> {

        val db = this.readableDatabase
        val todos:ArrayList<Todo> = ArrayList<Todo>()
        val query = "SELECT * FROM $TABLE_NAME"
        var cursor: Cursor? = null

        try{
            cursor = db.rawQuery(query, null)
        }catch (e: SQLiteException) {
            db.execSQL(query)
            return ArrayList()
        }

        if (cursor.moveToFirst()) {
            do {
                var id = cursor.getColumnIndex(ID_COL)
                val todoTitle = cursor.getColumnIndex(TODO_TITLE)
                val todoDate = cursor.getColumnIndex(DATE)
                var status = cursor.getColumnIndex(IS_CHECKED)

                var isChecked = false

                if (cursor.getInt(status) ==1) {
                    isChecked = true
                }

                var todo:Todo = Todo(
                    cursor.getInt(id),
                    cursor.getString(todoTitle),
                    cursor.getString(todoDate),
                    isChecked
                )

                todos.add(todo)

            } while (cursor.moveToNext())
        }

        return todos.asReversed()
    }

    fun deleteTodo(id:Int) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "id=?", arrayOf(id.toString()))
        db.close();
    }

    fun isChecked(id:Int, todoTitle:String, date:String, isChecked:Boolean) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ID_COL, id)
        values.put(TODO_TITLE, todoTitle)
        values.put(DATE, date)
        if(isChecked)
            values.put(IS_CHECKED, 1)
        else
            values.put(IS_CHECKED, 0)

        val whereClause = "id=?"
        val whereArgs = arrayOf(id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)

    }

    fun updateTableAfterDelete() {
        val todos:MutableList<Todo> = getTodos().asReversed()
        val db = this.writableDatabase
        db.execSQL("delete from "+ TABLE_NAME);

        for (todo in todos) {
            addTodo(todo)
        }
        db.close()

    }

    companion object{

        private const val DATABASE_NAME = "TODO_LIST"

        private const val DATABASE_VERSION = 1

        const val TABLE_NAME = "todos"

        const val ID_COL = "id"

        const val TODO_TITLE = "todotitle"

        const val DATE = "date"

        const val IS_CHECKED = "ischecked"

    }


}