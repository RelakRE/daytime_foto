package ru.gb.daytime_photo.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.gb.daytime_photo.databinding.FragmentCollapsingToolbarBinding

class CollapsingTollBarFragment : Fragment() {

    private var _binding: FragmentCollapsingToolbarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollapsingToolbarBinding.inflate(inflater, container, false)
        return binding.root
    }

}