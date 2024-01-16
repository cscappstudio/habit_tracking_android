package com.cscmobi.habittrackingandroid.presentation.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.cscmobi.habittrackingandroid.R

class FrequencyTextAdapter (context: Context, resource: Int, items: List<String>)  : ArrayAdapter<String>(context, resource, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Customize the view for the item at the given position
        val itemView = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.item_text_frequency_dropdown, parent, false
        )

        // Get the data item at the specified position
        val item = getItem(position)

        // Customize the view here based on the item data
        val textView = itemView.findViewById<AppCompatTextView>(R.id.txtCategory)
        textView.text = item

        return itemView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.item_autocomplete_textview, parent, false
        )


        val item = getItem(position)

        // Customize the view here based on the item data
        val textView = itemView.findViewById<TextView>(R.id.textView)
        textView.text = item

        return itemView
    }


}












