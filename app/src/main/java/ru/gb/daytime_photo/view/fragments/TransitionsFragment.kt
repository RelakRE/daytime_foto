package ru.gb.daytime_photo.view.fragments

import android.os.Bundle
import android.text.TextUtils.replace
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.transition.*
import ru.gb.daytime_photo.databinding.FragmentTransitionsBinding
import ru.gb.daytime_photo.R

class TransitionsFragment : Fragment() {

    private lateinit var binding: FragmentTransitionsBinding
    private var textIsVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransitionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonFade.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding.transitionsContainer, Fade())
            textIsVisible = !textIsVisible
            binding.text.isVisible = textIsVisible
        }
        binding.buttonChangeBounds.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding.transitionsContainer, ChangeBounds())
            textIsVisible = !textIsVisible
            binding.text.isVisible = textIsVisible
        }
        binding.buttonSlide.setOnClickListener {
            TransitionManager.beginDelayedTransition(
                binding.transitionsContainer,
                Slide(Gravity.END)
            )
            textIsVisible = !textIsVisible
            binding.text.isVisible = textIsVisible
        }
        binding.buttonTransitionSet.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding.transitionsContainer, TransitionSet())
            textIsVisible = !textIsVisible
            binding.text.isVisible = textIsVisible
        }
        binding.buttonExplode.setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                replace(R.id.container, ExplodeFragment())
                addToBackStack(null)
            }
        }
    }
}