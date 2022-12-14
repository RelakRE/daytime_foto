package ru.gb.daytime_photo.view.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.datepicker.MaterialDatePicker
import ru.gb.daytime_photo.*
import ru.gb.daytime_photo.databinding.FragmentPictureOfTheDayBinding
import ru.gb.daytime_photo.model.retrofits.nasa_apod.PODNasaAPOD
import ru.gb.daytime_photo.viewmodel.PictureOfTheDayData
import ru.gb.daytime_photo.viewmodel.PictureOfTheDayViewModel
import java.time.Instant
import java.time.ZoneId


class PictureOfTheDayFragment : Fragment() {

    private var _binding: FragmentPictureOfTheDayBinding? = null
    private val binding get() = _binding!!

    private var lastConditionDayChips = R.id.chips_today

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
        setBottomSheetBehavior(binding.bottomSheetImageInfo.root)
        setFab()
        bindChips()

        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }
    }

    private fun bindChips() {

        binding.HDChips.setOnClickListener {
            fetchDate()
        }
        binding.chipsToday.setOnClickListener {
            if (lastConditionDayChips != binding.chipsToday.id) {
                viewModel.setDay(viewModel.TODAY_DATE)
                fetchDate()
                lastConditionDayChips = binding.chipsToday.id
            }
        }
        binding.chipsYesterday.setOnClickListener {
            if (lastConditionDayChips != binding.chipsYesterday.id) {
                viewModel.setDay(viewModel.YESTERDAY_DATE)
                fetchDate()
                lastConditionDayChips = binding.chipsYesterday.id
            }
        }
        binding.chipsDate.setOnClickListener {
            if (lastConditionDayChips != binding.chipsDate.id) {
                if (viewModel.dateOnDateChips == null) {
                    showDatePicker()
                } else {
                    viewModel.setDay(viewModel.dateOnDateChips!!)
                    lastConditionDayChips = binding.chipsYesterday.id
                }
                fetchDate()
            }
        }
        binding.chipsDate.setOnLongClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker(): Boolean {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("???????????????? ????????")
                .build()

        datePicker.show(requireActivity().supportFragmentManager, "tag")
        datePicker.addOnPositiveButtonClickListener {
            viewModel.dateOnDateChips =
                Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate();
            binding.chipsDate.text = datePicker.headerText
        }
        return true
    }


    private fun fetchDate() {
        viewModel.getData()
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
                        startActivity(
                            Intent(
                                requireActivity(),
                                ViewPagerActivity::class.java
                            )
                        )
                        return true
                    }
                    R.id.app_bar_settings -> {
                        activity.supportFragmentManager.beginTransaction()
                            .add(R.id.container, SettingsFragment.newInstance())
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
                    R.id.app_bar_bottom_navigation -> {
                        startActivity(
                            Intent(
                                requireActivity(),
                                BottomNavigationActivity::class.java
                            )
                        )
                        return true
                    }
                    else -> return false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun renderData(data: PictureOfTheDayData) {
        when (data) {
            is PictureOfTheDayData.SuccessDayPhoto -> {
                renderImageOrVideo(data)
            }
            is PictureOfTheDayData.Loading -> {
                //???????????????????? ????????????????
                //showLoading()
                toast("????????????????")
            }
            is PictureOfTheDayData.Error -> {
                //???????????????????? ????????????
                showError(data)
            }
        }
    }

    private fun renderImageOrVideo(data: PictureOfTheDayData.SuccessDayPhoto) {
        val serverResponseData = data.serverResponseData
        when (serverResponseData.mediaType) {
            ("image") -> {
                binding.youTubePlayer.isInvisible = true
                binding.imageView.isInvisible = false
                checkShowImageUrl(serverResponseData)
            }
            ("video") -> {
                binding.youTubePlayer.isInvisible = false
                binding.imageView.isInvisible = true
                initializeVideo(serverResponseData)
            }
        }
        configInfoBox(serverResponseData)
        configBottomSheetInfo(serverResponseData)
    }

    private fun initializeVideo(serverResponseData: PODNasaAPOD) {
        val youTubeFragment = viewModel.youTubeFragment?: YouTubeFragment()
        youTubeFragment.url =
            youTubeFragment.getUrlId(serverResponseData.url as String)

        childFragmentManager.commit {
            replace(R.id.you_tube_player, youTubeFragment)
        }

        youTubeFragment.initialize(BuildConfig.YOUTUBE_KEY, youTubeFragment)
    }

    private fun showError(data: PictureOfTheDayData.Error) {
        binding.titleImage.text = getText(R.string.error)
        binding.secondaryTextImage.text = data.error.message
        binding.imageView.load(R.drawable.ic_load_error_vector) {
            lifecycle(this@PictureOfTheDayFragment)
            error(R.drawable.ic_load_error_vector)
            placeholder(R.drawable.ic_no_photo_vector)
            crossfade(true)
        }
    }

    private fun configBottomSheetInfo(serverResponseData: PODNasaAPOD) {
        binding.bottomSheetImageInfo.bottomSheetDescriptionHeader.text = serverResponseData.title
        binding.bottomSheetImageInfo.bottomSheetDescription.text = serverResponseData.explanation
    }

    private fun configInfoBox(serverResponseData: PODNasaAPOD) {
        binding.titleImage.text = serverResponseData.title
        binding.secondaryTextImage.text = serverResponseData.explanation
    }

    private fun checkShowImageUrl(serverResponseData: PODNasaAPOD) {
        val url =
            if (binding.HDChips.isChecked) serverResponseData.hdurl else serverResponseData.url
        if (url.isNullOrEmpty()) {
            //???????????????????? ????????????
            //showError("??????????????????, ?????? ???????????? ????????????")
            toast("Link is empty")
        } else {
            //???????????????????? ????????
            //showSuccess()
            //Coil ?? ????????????: ???????????????????? ?????????????? ?? ???????????? ImageView ???????????? extension-?????????????? ?? ???????????????? ???????????? ???? ??????????????????????
            //?? ?? ???????????? ?????????????? ???????????????????????????? ?????????????????? (???? ??????????????????????) ?????? ?????????????????????? ????????????, ???????????????? ????????????????, ???????????????? ?????????? ??????????????????????
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
