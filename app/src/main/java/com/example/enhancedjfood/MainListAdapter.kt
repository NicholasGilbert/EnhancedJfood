package com.example.enhancedjfood

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView

class MainListAdapter : BaseExpandableListAdapter {
    constructor(inContext: MenuFragment,
                inListSeller: ArrayList<String>,
                inChildMapping: HashMap<String, ArrayList<Food>>) : super() {
        check = 1
        _contextHolder1 = inContext
        _listSeller = inListSeller
        _childMapping = inChildMapping
    }
    constructor(inContext: MenuByCategoryFragment,
                inListSeller: ArrayList<String>,
                inChildMapping: HashMap<String, ArrayList<Food>>) : super() {
        check = 2
        _contextHolder2 = inContext
        _listSeller = inListSeller
        _childMapping = inChildMapping
    }

    var check: Int = 0
    lateinit var _contextHolder1: MenuFragment
    lateinit var _contextHolder2: MenuByCategoryFragment
    var _listSeller: ArrayList<String>
    var _childMapping: HashMap<String, ArrayList<Food>>

    override fun getGroup(groupPosition: Int): String  {
        return _listSeller.get(groupPosition)
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, inConvertView: View?, parent: ViewGroup?): View {
        val headerTitle: String = getGroup(groupPosition)
        var convertView = inConvertView
        if (convertView == null){
            if (check == 1) {
                val infalInflater: LayoutInflater = _contextHolder1.layoutInflater
                convertView = infalInflater.inflate(R.layout.layout_seller, null)
            }
            else if (check == 2) {
                val infalInflater: LayoutInflater = _contextHolder2.layoutInflater
                convertView = infalInflater.inflate(R.layout.layout_seller, null)
            }
        }

        val lblListHeader : TextView = convertView!!.findViewById(R.id.lblListHeader)
        lblListHeader.setTypeface(null, Typeface.BOLD)
        lblListHeader.setText(headerTitle)

        return convertView
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return _childMapping.get(_listSeller.get(groupPosition))!!.size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Food {
        return _childMapping.get(_listSeller.get(groupPosition))!!.get(childPosition)
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, inConvertView: View?, parent: ViewGroup?): View {
        val childText: Food = getChild(groupPosition, childPosition)
        var convertView = inConvertView
        if (convertView == null){
            if (check == 1) {
                val infalInflater: LayoutInflater = _contextHolder1.layoutInflater
                convertView = infalInflater.inflate(R.layout.layout_food, null)
            }
            else if (check == 2) {
                val infalInflater: LayoutInflater = _contextHolder2.layoutInflater
                convertView = infalInflater.inflate(R.layout.layout_food, null)
            }
        }

        val txtListChild: TextView = convertView!!.findViewById(R.id.lblListItem)
        txtListChild.setText(childText.foodName)
        return convertView
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return _listSeller.size
    }
}