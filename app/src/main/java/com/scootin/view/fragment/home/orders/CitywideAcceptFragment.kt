package com.scootin.view.fragment.home.orders

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.dhaval2404.imagepicker.ImagePicker
import com.scootin.R
import com.scootin.databinding.FragmentCitywideAcceptOrderBinding
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.glide.GlideApp
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.RequestCitywideOrder
import com.scootin.network.response.Media
import com.scootin.util.fragment.autoCleared
import com.scootin.view.fragment.home.BaseFragment
import com.scootin.viewmodel.order.OrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class CitywideAcceptFragment: BaseFragment(R.layout.fragment_citywide_accept_order) {
    private var binding by autoCleared<FragmentCitywideAcceptOrderBinding>()

    private val viewModel: OrdersViewModel by viewModels()

    @Inject
    lateinit var appExecutors: AppExecutors

    private val args: CitywideAcceptFragmentArgs by navArgs()

    private var media: Media? = null

    private val orderId by lazy {
        args.orderId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCitywideAcceptOrderBinding.bind(view)
        binding.lifecycleOwner = this
        binding.pendingIcon.setImageResource(R.drawable.ic_pending_icon)

        // Timber.i("Order Detail is loading for element $args and bundle $savedInstanceState")
        binding.txtItemList.setOnClickListener {
            onClickOfUploadMedia()
        }

        viewModel.getCitywideOrder(args.orderId).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.data = it.data
                    if(it.data?.message!=null){
                        binding.instructionTxt.visibility=View.VISIBLE
                        binding.instructionTxt.text="Additional Instruction/Remark: "+it.data.message
                    }
                }
                Status.ERROR -> {

                }
                Status.LOADING -> {

                }
            }
        }

        binding.acceptButton.setOnClickListener {
            showLoading()

            if (media == null) {
                Toast.makeText(context, "Invalid Media", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val weight = binding.weightOfItem.text?.toString()?.toIntOrNull()

            if (weight == null) {
                Toast.makeText(context, "Invalid Weight", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.getCitywideOrder(
                AppHeaders.userID, orderId.toString(), RequestCitywideOrder(
                    media!!.id, weight)
            ).observe(viewLifecycleOwner) {
                when(it.status) {
                    Status.SUCCESS -> {
                        dismissLoading()
                        Toast.makeText(requireContext(), "Order Accepted", Toast.LENGTH_LONG).show()
                        enableOrDisableVisibility(true)
                        findNavController().popBackStack(R.id.nav_pending_orders, false)
                    }
                    Status.ERROR -> {
                        dismissLoading()
                        Toast.makeText(requireContext(), "This Order has been Accepted by other rider", Toast.LENGTH_LONG).show()
                        enableOrDisableVisibility(true)
                    }
                    Status.LOADING -> {

                    }
                }

            }
        }



    }


    private fun enableOrDisableVisibility(completed: Boolean) {
        if (completed) {
            binding.acceptButton.visibility = View.GONE
        }
    }
    private fun onClickOfUploadMedia() {
        ImagePicker.with(this)
            .compress(1024)
            .maxResultSize(1080, 1080)
            .start { resultCode, data ->
                if (resultCode == Activity.RESULT_OK) {
                    uploadMedia(ImagePicker.getFile(data))
                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun uploadMedia(file: File?) {
        showLoading()
        file?.let {
            viewModel.uploadMedia(it).observe(viewLifecycleOwner) {response->
                Timber.i("Media viewModel.uploadMedia ${response.isSuccessful}")
                if(response.isSuccessful) {
                    dismissLoading()
                    val media = response.body() ?: return@observe
                    this.media = media
                    loadMedia()
                } else {
                    Toast.makeText(context, "There is some error media", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun loadMedia() {
        if (media?.url != null) {
            GlideApp.with(requireContext()).load(media?.url).into(binding.imageMedia)
        }
    }

}