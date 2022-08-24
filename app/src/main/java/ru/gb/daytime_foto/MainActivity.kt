package ru.gb.daytime_foto

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commitNow
import ru.gb.daytime_foto.databinding.ActivityMainBinding
import ru.gb.daytime_foto.view.MainFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.commitNow {
                replace(R.id.container, MainFragment.newInstance())
            }
        }
    }
}