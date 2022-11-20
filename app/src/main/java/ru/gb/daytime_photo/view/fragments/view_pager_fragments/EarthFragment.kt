package ru.gb.daytime_photo.view.fragments.view_pager_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import ru.gb.daytime_photo.R
import ru.gb.daytime_photo.databinding.FragmentEarthBinding
import ru.gb.daytime_photo.viewmodel.PictureOfEarthData
import ru.gb.daytime_photo.viewmodel.PictureOfTheEarthViewModel

class EarthFragment : Fragment() {

    private var _binding: FragmentEarthBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PictureOfTheEarthViewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(PictureOfTheEarthViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewModel.getData().observe(viewLifecycleOwner) { renderData(it) }

        _binding = FragmentEarthBinding.inflate(inflater, container, false)

        return binding.root
    }

    private fun renderData(data: PictureOfEarthData) {
        when (data) {
            is PictureOfEarthData.Loading -> {
                toast("Загрузка")
            }
            is PictureOfEarthData.Error -> {
                toast("Ошибка")
            }
            is PictureOfEarthData.SuccessEarthPhoto -> {
                binding.imageViewEarth.load(data.url) {
                    lifecycle(this@EarthFragment)
                    error(R.drawable.ic_load_error_vector)
                    placeholder(R.drawable.ic_no_photo_vector)
                    crossfade(true)
                }

            }
        }

    }

    private fun Fragment.toast(string: String?) {
        Toast.makeText(requireContext(), string, Toast.LENGTH_SHORT).apply {
            show()
        }
    }
}