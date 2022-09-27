package ru.gb.daytime_photo.view.view_pager_fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val fragments = arrayOf(EarthFragment(), MarsFragment(), WeatherFragment())

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> fragments[EARTH_FRAGMENT]
            1 -> fragments[MARS_FRAGMENT]
            2 -> fragments[WEATHER_FRAGMENT]
            else -> fragments[EARTH_FRAGMENT]
        }
    }

    override fun getItemCount(): Int {
        return fragments.size
    }

//    override fun getPageTitle(position: Int): CharSequence {
//        return when (position) {
//            EARTH_FRAGMENT -> "Earth"
//            MARS_FRAGMENT -> "Mars"
//            WEATHER_FRAGMENT -> "Weather"
//            else -> "Earth"
//        }
//    }


    companion object {
        private const val EARTH_FRAGMENT = 0
        private const val MARS_FRAGMENT = 1
        private const val WEATHER_FRAGMENT = 2
    }
}