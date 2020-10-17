package com.appchamp.dragdroptwolists

import android.content.ClipData
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

interface CustomListener {
    fun setEmptyList(visibility: Int, recyclerView: Int, emptyTextView: Int)
}

class CustomAdapter(private var list: List<String>, private val listener: CustomListener?)
    : RecyclerView.Adapter<CustomAdapter.CustomViewHolder?>(), View.OnTouchListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CustomViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int = list.size

    fun updateList(list: MutableList<String>) {
        this.list = list
    }

    fun getList(): MutableList<String> = this.list.toMutableList()

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                val data = ClipData.newPlainText("", "")
                val shadowBuilder = View.DragShadowBuilder(v)
                v?.startDragAndDrop(data, shadowBuilder, v, 0)
                return true
            }
        }
        return false
    }

    val dragInstance: DragListener?
        get() = if (listener != null) {
            DragListener(listener)
        } else {
            Log.e(javaClass::class.simpleName, "Listener not initialized")
            null
        }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.text?.text = list[position]
        holder.frameLayout?.tag = position
        holder.frameLayout?.setOnTouchListener(this)
        holder.frameLayout?.setOnDragListener(DragListener(listener!!))
    }

    class CustomViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.list_item, parent, false)) {
        var text: TextView? = null
        var frameLayout: FrameLayout? = null

        init {
            text = itemView.findViewById(R.id.text)
            frameLayout = itemView.findViewById(R.id.frame_layout_item)
        }
    }
}