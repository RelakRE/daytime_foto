package ru.gb.daytime_photo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.gb.daytime_photo.databinding.ActivityBottomNavigationViewBinding
import ru.gb.daytime_photo.view.view_pager_fragments.EarthFragment
import ru.gb.daytime_photo.view.view_pager_fragments.MarsFragment
import ru.gb.daytime_photo.view.view_pager_fragments.WeatherFragment

class BottomNavigationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBottomNavigationViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBottomNavigationViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.getOrCreateBadge(R.id.bottom_view_earth)
        val badge = binding.bottomNavigationView.getBadge(R.id.bottom_view_earth)
        badge?.number = 2

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_view_earth -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.bottom_navigation_container, EarthFragment())
                        .commit()
                    true
                }
                R.id.bottom_view_mars -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.bottom_navigation_container, MarsFragment())
                        .commit()
                    true
                }
                R.id.bottom_view_weather -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.bottom_navigation_container, WeatherFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }

        binding.bottomNavigationView.selectedItemId = R.id.bottom_view_mars
    }
}