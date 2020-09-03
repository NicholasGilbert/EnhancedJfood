package com.example.enhancedjfood

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView

class ListPesananAdapter (inContext: PesananFragment,
                          inListDate: ArrayList<String>,
                          inChildMapping: HashMap<String, ArrayList<Invoice>>) : BaseExpandableListAdapter() {
    val _context = inContext
    val _listDate = inListDate
    val _childMapping = inChildMapping

    override fun getGroup(groupPosition: Int): String {
        return _listDate.get(groupPosition)
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
        if (convertView == null) {
            val infalInflater: LayoutInflater = _context.layoutInflater
            convertView = infalInflater.inflate(R.layout.layout_seller, null)
        }

        val lblListHeader: TextView = convertView!!.findViewById(R.id.lblListHeader)
        lblListHeader.setTypeface(null, Typeface.BOLD)
        lblListHeader.setText(headerTitle.substring(0, 10))
        return convertView
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return _childMapping.get(_listDate.get(groupPosition))!!.size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Invoice {
        return _childMapping.get(_listDate.get(groupPosition))!!.get(childPosition)
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        inConvertView: View?,
        parent: ViewGroup?
    ): View {
        val childText: Invoice = getChild(groupPosition, childPosition)
        var convertView = inConvertView
        if (convertView == null) {
            val infalInflater: LayoutInflater = _context.layoutInflater
            convertView = infalInflater.inflate(R.layout.layout_food, null)
        }

        val txtListChild: TextView = convertView!!.findViewById(R.id.lblListItem)
        txtListChild.setText(childText.invoiceId.toString() + " - " + childText.invoicefood.foodName + ", " + childText.invoiceStatus.toString())
        return convertView
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return _listDate.size
    }
}