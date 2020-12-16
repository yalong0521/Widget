package com.larkin.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.larkin.view.loading.LoadingView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_activity_main_recycler.view.*

class MainActivity : AppCompatActivity() {
    private val mWidgets = arrayListOf<View>()
    private lateinit var mInflater: LayoutInflater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mInflater = LayoutInflater.from(this)
        mWidgets.add(getLoadingView())
        rv_widgets.adapter = RecyclerViewAdapter()
    }

    private fun getLoadingView(): View {
        val loadingView = LoadingView(this)
        loadingView.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        val dp20 = (resources.displayMetrics.density * 20).toInt()
        loadingView.setPadding(dp20, dp20, dp20, dp20)
        return loadingView
    }

    private inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.VH>() {
        inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val llContainer: LinearLayout = itemView.ll_container
            val tvWidgetName: TextView = itemView.tv_widget_name
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val itemLayout = R.layout.item_activity_main_recycler
            return VH(mInflater.inflate(itemLayout, parent, false))
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            val widget = mWidgets[position]
            holder.llContainer.addView(widget, 0)
            holder.tvWidgetName.text = widget.javaClass.simpleName
        }

        override fun getItemCount(): Int = mWidgets.size
    }
}