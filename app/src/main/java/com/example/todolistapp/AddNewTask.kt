package com.example.todolistapp

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.todolistapp.Model.ToDoModel
import com.example.todolistapp.Utils.DataBaseHelper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddNewTask : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "AddNewTask"

        fun newInstance(): AddNewTask {
            return AddNewTask()
        }
    }

    // widgets
    private lateinit var mEditText: EditText
    private lateinit var mSaveButton: Button

    private lateinit var myDb: DataBaseHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.add_new_task, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mEditText = view.findViewById(R.id.edittext)
        mSaveButton = view.findViewById(R.id.button_save)

        myDb = DataBaseHelper(requireActivity())

        var isUpdate = false

        val bundle = arguments
        if (bundle != null) {
            isUpdate = true
            val task = bundle.getString("task")
            mEditText.setText(task)

            if (task!!.length > 0) {
                mSaveButton.isEnabled = false
            }
        }

        mEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString() == "") {
                    mSaveButton.isEnabled = false
                    mSaveButton.setBackgroundColor(Color.GRAY)
                } else {
                    mSaveButton.isEnabled = true
                    mSaveButton.setBackgroundColor(resources.getColor(R.color.purple_500))
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        val finalIsUpdate = isUpdate
        mSaveButton.setOnClickListener {
            val text = mEditText.text.toString()

            if (finalIsUpdate) {
                myDb.updateTask(bundle!!.getInt("id"), text)
            } else {
                val item = ToDoModel()
                item.task = text
                item.status = 0
                myDb.insertTask(item)
            }
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val activity = activity
        if (activity is ONDialogCloseListener) {
            activity.onDialogClose(dialog)
        }
    }
}
