package com.example.timezonecalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import java.util.*

// -Auto Complete Adapter-
// --------------------Not in use yet!--------------------
// This class is for filtering time zone search results, since the default search only searches from the beginning of a word.
// (Search "CEST" doesn't find "CET/CEST (Europe)" and "US" doesn't find any of the US time zones for example.)

class AutoCompleteAdapter<T>(context: Context, listObjects: List<T>) : ArrayAdapter<T>(context, R.layout.support_simple_spinner_dropdown_item, listObjects), Filterable {
    private val listObjects: List<T>
    var suggestions: MutableList<T> = ArrayList()
    private val mFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filterResults = FilterResults()
            if (constraint != null) {
                suggestions.clear()
                for (`object` in listObjects) {
                    if (`object`.toString().contains(constraint.toString())) {
                        suggestions.add(`object`)
                    }
                }
                filterResults.values = suggestions
                filterResults.count = suggestions.size
            }
            return filterResults
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            if (results == null) {
                return
            }
            val filteredList = results.values as List<T>
            if (results.count > 0) {
                clear()
                for (filteredObject in filteredList) {
                    add(filteredObject)
                }
                notifyDataSetChanged()
            }
        }
    }

    override fun getFilter(): Filter {
        return mFilter
    }

    private class ViewHolder {
        var title: TextView? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val listObject: Any? = getItem(position)
        val viewHolder: ViewHolder // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = ViewHolder()
            val inflater = LayoutInflater.from(context)
            convertView = inflater.inflate(R.layout.support_simple_spinner_dropdown_item, parent, false)
            viewHolder.title = convertView!!.findViewById<View>(R.id.title) as TextView?
            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }
        viewHolder.title!!.text = listObject.toString()
        return convertView!!
    }

    init {
        this.listObjects = ArrayList(listObjects)
    }
}