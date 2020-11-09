package com.scootin.view.fragment.home.gallery

import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.scootin.R
import com.scootin.databinding.FragmentImageGalleryBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.Media
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.GalleryAdapter
import com.scootin.view.fragment.home.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ImageGalleryFragment : BaseFragment(R.layout.fragment_image_gallery) {

    private var binding by autoCleared<FragmentImageGalleryBinding>()
    @Inject
    lateinit var appExecutors: AppExecutors

    private val args: ImageGalleryFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fullScreenListener.showHideActionBar(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentImageGalleryBinding.bind(view)
        updateView()
    }


    private fun updateView() {
        val galleryAdapter = GalleryAdapter(
            appExecutors,
            object : GalleryAdapter.ImageGalleryCallback {
                override fun onImageChanged(@ColorInt color: Int) {
                }

                override fun onImageScaleChanged(isNormal: Boolean) {
                }
            }
        )


        binding.viewPager2.apply {
            adapter = galleryAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    val currentItem = galleryAdapter.currentList[position]
                    val color = currentItem.colorTint
                    val toolbarColor = ContextCompat.getColor(requireContext(),
                        if (currentItem.isWhite) R.color.white else R.color.black
                    )
                    binding.toolbarClose.setColorFilter(toolbarColor, PorterDuff.Mode.SRC_ATOP)
                }
            })
        }

        galleryAdapter.submitList(
            mutableListOf<Media>().apply {
                add(args.imagedata)
            }
        )

        binding.toolbarClose.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDetach() {
        super.onDetach()
        fullScreenListener.showHideActionBar(true)
    }
}