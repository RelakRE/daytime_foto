package ru.gb.daytime_photo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.gb.daytime_photo.databinding.ActivityViewPagerBinding
import ru.gb.daytime_photo.view.view_pager_fragments.ViewPagerAdapter


class ViewPagerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewPagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager = binding.viewPager

        viewPager.adapter = ViewPagerAdapter(supportFragmentManager)
        binding.indicator.setViewPager(viewPager)
        configTabLayout()

    }

    private fun configTabLayout() {
        binding.apply {
            tabLayout.setupWithViewPager(viewPager)
            tabLayout.getTabAt(EARTH)?.setIcon(R.drawable.ic_earth)
            tabLayout.getTabAt(MARS)?.setIcon(R.drawable.ic_mars)
            tabLayout.getTabAt(WEATHER)?.setIcon(R.drawable.ic_system)
        }
    }

    companion object {
        private const val EARTH = 0
        private const val MARS = 1
        private const val WEATHER = 2
    }

}