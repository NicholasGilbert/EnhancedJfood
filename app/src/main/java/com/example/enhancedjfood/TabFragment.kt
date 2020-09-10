package com.example.enhancedjfood

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_tab.*

class TabFragment : Fragment(R.layout.fragment_tab) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val customerId: Int = arguments!!.getInt("customer")
        viewpager_main.adapter = TabAdapter(activity!!.supportFragmentManager, customerId)
        tabs_main.setupWithViewPager(viewpager_main)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_layout, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}