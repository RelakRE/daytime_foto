package ru.gb.daytime_photo.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import ru.gb.daytime_photo.App
import ru.gb.daytime_photo.R
import ru.gb.daytime_photo.databinding.SettingsFragmentBinding

class SettingsFragment : Fragment() {

    private var _binding: SettingsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (App.appTheme) {
            R.style.PinkTheme -> binding.pinkTheme.isChecked = true
            R.style.AppTheme -> binding.defaultTheme.isChecked = true
        }

        binding.chipGroupTheme.setOnCheckedStateChangeListener { chipGroup, position ->
            chipGroup.findViewById<Chip>(position.first())?.let {
                when (it.id) {
                    R.id.pink_theme -> App.appTheme = R.style.PinkTheme
                    R.id.default_theme -> App.appTheme = R.style.AppTheme
                }
                requireActivity().recreate()
            }
        }

        binding.chipClose.setOnCloseIconClickListener {
            Toast.makeText(
                context,
                "Close is Clicked",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}
