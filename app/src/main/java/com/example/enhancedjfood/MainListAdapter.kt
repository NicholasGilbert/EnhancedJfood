package com.example.enhancedjfood

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView

class MainListAdapter(inContext: MenuFragment,
                      inListSeller: ArrayList<Seller>,
                      inChildMapping: HashMap<Seller, ArrayList<Food>>) : BaseExpandableListAdapter() {

    val _context = inContext
    val _listSeller = inListSeller
    val _childMapping = inChildMapping

    override fun getGroup(groupPosition: Int): Seller  {
        return _listSeller.get(groupPosition)
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, inConvertView: View?, parent: ViewGroup?): View {
        val headerTitle: Seller = getGroup(groupPosition)
        var convertView = inConvertView
        if (convertView == null){
            val infalInflater: LayoutInflater = _context.layoutInflater
            convertView = infalInflater.inflate(R.layout.layout_seller, null)
        }

        val lblListHeader : TextView = convertView!!.findViewById(R.id.lblListHeader)
        lblListHeader.setTypeface(null, Typeface.BOLD)
        lblListHeader.setText(headerTitle.sellerName)
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
            val infalInflater: LayoutInflater = _context.layoutInflater
            convertView = infalInflater.inflate(R.layout.layout_food, null)
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