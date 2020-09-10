package com.example.enhancedjfood

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class TabAdapter(fm: FragmentManager, inId: Int): FragmentPagerAdapter(fm) {
    private val pages = listOf(
        MenuFragment(),
        MenuByCategoryFragment()
    )
    val customerId = inId

    override fun getItem(position: Int): Fragment {
        val thePage = pages[position]
        val bundle: Bundle = Bundle()
        bundle.putInt("customer", customerId)
        thePage.arguments =  bundle
        return thePage
    }

    override fun getCount(): Int {
        return pages.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "First Tab"
            else -> "Second Tab"
        }
    }
}