package ru.gb.daytime_photo

import android.R
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

        val viewPager =  binding.viewPager

        viewPager.adapter = ViewPagerAdapter(supportFragmentManager)
        binding.indicator.setViewPager(viewPager)


// optional
//        adapter.registerDataSetObserver(indicator.dataSetObserver)
    }

}