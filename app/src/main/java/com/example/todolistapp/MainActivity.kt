package com.example.todolistapp

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistapp.Adapter.ToDoAdapter
import com.example.todolistapp.Model.ToDoModel
import com.example.todolistapp.Utils.DataBaseHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity(), ONDialogCloseListener {
    private var mRecyclerview: RecyclerView? = null
    private var fab: FloatingActionButton? = null
    private var myDB: DataBaseHelper? = null
    private var mList: List<ToDoModel>? = null
    private var adapter: ToDoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mRecyclerview = findViewById(R.id.recyclerview)
        fab = findViewById(R.id.fab)
        myDB = DataBaseHelper(this@MainActivity)
        mList = ArrayList()
        adapter = ToDoAdapter(mList as ArrayList<ToDoModel>, this@MainActivity, myDB!!)
        mRecyclerview!!.setHasFixedSize(true)
        mRecyclerview!!.setLayoutManager( LinearLayoutManager(this))
        mRecyclerview!!.setAdapter(adapter)

        mList = myDB?.getAllTasks()
        mList?.reversed()
        mList?.let { adapter?.setTasks(it) }

        fab!!.setOnClickListener {
           AddNewTask.newInstance().show(supportFragmentManager, AddNewTask.TAG)
        }
        val itemTouchHelper = ItemTouchHelper(RecyclerViewTouchHelper(adapter!!))
        itemTouchHelper.attachToRecyclerView(mRecyclerview)

    }

    override fun onDialogClose(dialogInterface: DialogInterface?) {
        mList = myDB?.getAllTasks()
        mList?.reversed()
        mList?.let { adapter?.setTasks(it) }
        adapter?.notifyDataSetChanged()
    }
}