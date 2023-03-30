package com.example.todolistapp.Adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistapp.AddNewTask
import com.example.todolistapp.MainActivity
import com.example.todolistapp.Model.ToDoModel
import com.example.todolistapp.R
import com.example.todolistapp.Utils.DataBaseHelper

class ToDoAdapter(private var mList: List<ToDoModel>, private val activity: MainActivity, private val myDB: DataBaseHelper)
    : RecyclerView.Adapter<ToDoAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = mList[position]
        holder.mCheckBox.text = item.task
        holder.mCheckBox.isChecked = toBoolean(item.status)
        holder.mCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                myDB.updateStatus(item.id, 1)
            } else {
                myDB.updateStatus(item.id, 0)
            }
        }
    }

    private fun toBoolean(num: Int): Boolean {
        return num != 0
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mCheckBox: CheckBox = itemView.findViewById(R.id.mcheckbox)

        init {
            mCheckBox.setOnClickListener {
                if (mCheckBox.isChecked) {
                    deletTask(adapterPosition)
                } else {
                    editItem(adapterPosition)
                }
            }
        }
    }

    fun getContext(): Context? {
        return activity
    }

    fun setTasks(mList: List<ToDoModel>) {
        this.mList = mList
        notifyDataSetChanged()
    }

    fun deletTask(position: Int) {
        val item = mList[position]
        myDB.deleteTask(item.id)
        mList = mList.filter { it != item }
        notifyItemRemoved(position)
    }

    fun editItem(position: Int) {
        val item = mList[position]
        val bundle = Bundle()
        bundle.putInt("id", item.id)
        bundle.putString("task", item.task)
        val task = AddNewTask()
        task.arguments = bundle
        task.show(activity.supportFragmentManager, task.tag)
    }



}