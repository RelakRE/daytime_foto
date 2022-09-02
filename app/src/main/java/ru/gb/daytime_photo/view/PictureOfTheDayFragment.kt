package ru.gb.daytime_photo.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import ru.gb.daytime_photo.MainActivity
import ru.gb.daytime_photo.R
import ru.gb.daytime_photo.databinding.FragmentPictureOfTheDayBinding
import ru.gb.daytime_photo.model.PODServerResponseData
import ru.gb.daytime_photo.viewmodel.PictureOfTheDayData
import ru.gb.daytime_photo.viewmodel.PictureOfTheDayViewModel

class PictureOfTheDayFragment : Fragment() {

    private var _binding: FragmentPictureOfTheDayBinding? = null
    private val binding get() = _binding!!

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(PictureOfTheDayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getData()
            .observe(viewLifecycleOwner) { renderData(it) }

        _binding = FragmentPictureOfTheDayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBottomMenu(view)

//        setBottomSheetBehavior(view.findViewById(R.id.bottom_sheet_container))
        setBottomSheetBehavior(binding.bottomSheetImageInfo.root)
        setFab()

        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }
        binding.HDChips.setOnClickListener { toast("sfsdfsfsf") }
    }

    private fun setBottomMenu(view: View) {

        val activity = requireActivity()
        val menuHost: MenuHost = activity
        val context = activity as MainActivity

        context.setSupportActionBar(view.findViewById(R.id.bottom_app_bar))

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_bottom_bar, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.app_bar_fav -> {
                        toast("Favourite")
                        return true
                    }
                    R.id.app_bar_settings -> {
                        activity.supportFragmentManager.beginTransaction()
                            .add(R.id.container, ChipsFragment.newInstance())
                            .addToBackStack(null)
                            .commit()
                        return true
                    }
                    android.R.id.home -> {
                        activity.let {
                            BottomNavigationDrawerFragment().show(it.supportFragmentManager, "tag")
                        }
                        return true
                    }
                    else -> return false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun renderData(data: PictureOfTheDayData) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                val serverResponseData = data.serverResponseData
                checkShowImageUrl(serverResponseData)
                configInfoBox(serverResponseData)
                configBottomSheetInfo(serverResponseData)
            }
            is PictureOfTheDayData.Loading -> {
                //Отобразите загрузку
                //showLoading()
                toast("Загрузка")
            }
            is PictureOfTheDayData.Error -> {
                //Отобразите ошибку
                //showError(data.error.message)
                toast(data.error.message)
            }
        }
    }

    private fun configBottomSheetInfo(serverResponseData: PODServerResponseData) {
        binding.bottomSheetImageInfo.bottomSheetDescriptionHeader.text = serverResponseData.title
        binding.bottomSheetImageInfo.bottomSheetDescription.text = serverResponseData.explanation
    }

    private fun configInfoBox(serverResponseData: PODServerResponseData) {
        binding.titleImage.text = serverResponseData.title
//        binding.secondaryTextImage.text = serverResponseData.explanation
    }

    private fun checkShowImageUrl(serverResponseData: PODServerResponseData) {
        val url = serverResponseData.url
        if (url.isNullOrEmpty()) {
            //Отобразите ошибку
            //showError("Сообщение, что ссылка пустая")
            toast("Link is empty")
        } else {
            //Отобразите фото
            //showSuccess()
            //Coil в работе: достаточно вызвать у нашего ImageView нужную extension-функцию и передать ссылку на изображение
            //а в лямбде указать дополнительные параметры (не обязательно) для отображения ошибки, процесса загрузки, анимации смены изображений
            binding.imageView.load(url) {
                lifecycle(this@PictureOfTheDayFragment)
                error(R.drawable.ic_load_error_vector)
                placeholder(R.drawable.ic_no_photo_vector)
                crossfade(true)
            }
        }
    }

    private fun setFab() {
        val context = requireActivity() as MainActivity
        binding.fab.setOnClickListener {
            if (isMain) {
                isMain = false
                binding.bottomAppBar.navigationIcon = null
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                binding.fab.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_back_fab
                    )
                )
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar_other_screen)
            } else {
                isMain = true
                binding.bottomAppBar.navigationIcon =
                    ContextCompat.getDrawable(context, R.drawable.ic_hamburger_menu_bottom_bar)
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                binding.fab.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_plus_fab
                    )
                )
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar)
            }
        }
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun Fragment.toast(string: String?) {
        Toast.makeText(requireContext(), string, Toast.LENGTH_SHORT).apply {
            show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PictureOfTheDayFragment()
        private var isMain = true
    }
}
