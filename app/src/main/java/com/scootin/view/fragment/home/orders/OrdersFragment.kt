package com.scootin.view.fragment.home.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentCompletedOrdersBinding
import com.scootin.network.AppExecutors
import com.scootin.network.response.AllOrdersList
import com.scootin.network.response.PendingOrdersList
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.orders.AllOrdersAdapter
import com.scootin.view.adapter.orders.CompletedOrdersAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class OrdersFragment:Fragment(R.layout.fragment_orders) {
        private var binding by autoCleared<FragmentCompletedOrdersBinding>()

        @Inject
        lateinit var appExecutors: AppExecutors
        private lateinit var allOrdersAdapter: AllOrdersAdapter

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            binding = FragmentCompletedOrdersBinding.bind(view)
            setAdaper()
            allOrdersAdapter.submitList(setList())

        }

        private fun setAdaper() {
            allOrdersAdapter =
                AllOrdersAdapter(
                    appExecutors,
                    object : AllOrdersAdapter.ItemAdapterClickLister {
                        override fun onCompletedOrderSelected(view: View) {

                            findNavController().navigate(OrdersFragmentDirections.actionAllOrdersFragmentToCompletedOrderFragment())
                        }

                        override fun onPendingOrderSelected(view: View) {
                           // findNavController().navigate(OrdersFragmentDirections.actionAllOrdersFramentgToPendingOrdersFragment())
                        }

                    })

            binding.recyclerView.apply {
                adapter = allOrdersAdapter

            }
        }


        private fun setList(): ArrayList<AllOrdersList> {
            val list = ArrayList<AllOrdersList>()
            list.add(
                AllOrdersList(
                    "DBP/00009",
                    true,
                    "20-10-05",
                    0
                )
            )
            list.add(
                AllOrdersList(
                    "DBP/00009",
                    false,
                    "20-10-05",
                    0
                )
            )
            list.add(
                AllOrdersList(
                    "DBP/00009",
                    false,
                    "20-10-05",
                    0
                )
            )
            list.add(
                AllOrdersList(
                    "DBP/00009",
                    true,
                    "20-10-05",
                    0
                )
            )


            return list
        }



    }
