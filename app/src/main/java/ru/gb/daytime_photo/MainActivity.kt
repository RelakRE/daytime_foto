package ru.gb.daytime_photo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commitNow
import ru.gb.daytime_photo.databinding.ActivityMainBinding
import ru.gb.daytime_photo.view.CollapsingTollBarFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTheme(App.appTheme)

        if (savedInstanceState == null) {
            supportFragmentManager.commitNow {
//                replace(R.id.container, PictureOfTheDayFragment.newInstance())
                replace(R.id.container, CollapsingTollBarFragment())
            }
        }
    }
}