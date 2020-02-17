package com.example.beginnertutorials

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatAutoCompleteTextView

class CustomView(context: Context,attrs: AttributeSet) : LinearLayout(context,attrs){

    private var actv: AppCompatAutoCompleteTextView
    private var listView: ListView
    private var title: TextView
    private var add: ImageView

    private var selectedItems: MutableList<String> = ArrayList()
    private var allItems: MutableList<String> = ArrayList()

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.custom_view_layout,this,true)
        actv = view.findViewById(R.id.actv)
        listView = view.findViewById(R.id.list_view)
        title = view.findViewById(R.id.tv_title)
        add = view.findViewById(R.id.iv_add)

        actv.threshold = 1

        add.setOnClickListener {
            val selectedString = actv.text.trim().toString()

            when{
                selectedString.isEmpty() ->
                    Toast.makeText(context,"Please enter data...",Toast.LENGTH_SHORT).show()
                selectedItems.contains(selectedString) ->
                    Toast.makeText(context,"Item already added",Toast.LENGTH_SHORT).show()
                else -> {
                    selectedItems.add(0,selectedString)
                    refreshData(true)
                }
            }
        }
    }

    fun setData(data: MutableList<String>){
        allItems = data
        actv.setAdapter(ArrayAdapter(context,android.R.layout.simple_list_item_1,allItems))
    }

    fun setTitle(str: String){
        title.text = str
    }

    fun getSelectedData(): MutableList<String> = selectedItems

    private fun refreshData(clearData: Boolean){
        listView.adapter = CustomViewAdapter(context,R.layout.custom_view_item,selectedItems)
        setListViewHeightBasedOnChildren(listView)
        if (clearData)
            actv.setText("")


    }

    private fun setListViewHeightBasedOnChildren(listView: ListView){
        var listAdapter = listView.adapter ?: return
        var totalHeight = listView.paddingTop + listView.paddingBottom
        for (i in 0 until listAdapter.count){
            val listItem = listAdapter.getView(i,null,listView)
            (listView as? ViewGroup)?.layoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT
            )
            listItem.measure(0,0)
            totalHeight += listItem.measuredHeight
        }
        val params = listView.layoutParams
        params.height = totalHeight + listView.dividerHeight * (listAdapter.count - 1)
        listView.layoutParams = params
    }

    inner class CustomViewAdapter(
        context: Context,
        var resource: Int,
        var objects: MutableList<String>
    ): ArrayAdapter<String>(context, resource, objects){
        private val inflater :LayoutInflater = LayoutInflater.from(context)

        override fun getCount(): Int = objects.size

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

            val view = inflater.inflate(resource,parent,false)

            val name = view.findViewById<TextView>(R.id.tv_name)
            val delete = view.findViewById<ImageView>(R.id.iv_delete)

            name.text = objects[position]
            delete.setOnClickListener {
                selectedItems.removeAt(
                    position
                )
                refreshData(false)
            }

            return view
        }

    }
}