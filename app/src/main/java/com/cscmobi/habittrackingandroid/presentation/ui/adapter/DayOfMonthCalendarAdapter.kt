package com.cscmobi.habittrackingandroid.presentation.ui.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.cscmobi.habittrackingandroid.R

class DayOfMonthCalendarAdapter(private val context: Context, private val days: List<Day>) : BaseAdapter() {

    var colorSelect: Int = -1
    override fun getCount(): Int {
        return days.size
    }

    override fun getItem(position: Int): Any {
        return days[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val day = days[position]

        val inflater = LayoutInflater.from(context)
        val view = convertView ?: inflater.inflate(R.layout.item_text_dayofmonth, parent, false)
        val frameContainer = view.findViewById<FrameLayout>(R.id.fr_container)

        val textView = view.findViewById<TextView>(R.id.txt_day)

        textView.text = day.date.toString()
//        textView.layoutParams = ViewGroup.LayoutParams(
//            ViewGroup.LayoutParams.WRAP_CONTENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT
//        )
//        textView.setPadding(10,5,10,5)
//        textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
//        textView.typeface = ResourcesCompat.getFont(context,R.font.worksans_medium)
//        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(com.intuit.ssp.R.dimen._10ssp))

//        textView.setBackgroundResource(android.R.color.transparent)

            if (day.isSelected) {
                textView.setTextColor(ContextCompat.getColor(context, R.color.white))
                frameContainer.setBackgroundResource(R.drawable.bg_circle)
                frameContainer.backgroundTintList = if (colorSelect != -1) ColorStateList.valueOf(colorSelect) else ColorStateList.valueOf(ContextCompat.getColor(context, R.color.orange))

            }
            else {
                textView.setTextColor(ContextCompat.getColor(context, R.color.bottle_green))
                frameContainer.background = null
            }

            frameContainer.setOnClickListener {
                day.isSelected = !day.isSelected
                notifyDataSetChanged()
            }



        return view
    }
}

data class Day(val date: Int, var isSelected: Boolean = false)
