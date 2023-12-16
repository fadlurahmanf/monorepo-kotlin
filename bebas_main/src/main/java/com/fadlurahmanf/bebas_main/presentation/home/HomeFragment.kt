package com.fadlurahmanf.bebas_main.presentation.home

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_main.data.dto.menu.TransactionMenuModel
import com.fadlurahmanf.bebas_main.databinding.FragmentHomeBinding
import com.fadlurahmanf.bebas_main.presentation.BaseMainFragment
import com.fadlurahmanf.bebas_main.presentation.home.adapter.MenuAdapter
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : BaseMainFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private var param1: String? = null
    private var param2: String? = null

    @Inject
    lateinit var viewModel: HomeFragmentViewModel

    lateinit var adapter: MenuAdapter
    private val menus: ArrayList<TransactionMenuModel> = arrayListOf()

    override fun injectFragment() {
        component.inject(this)
    }

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        viewModel.menuState.observe(this) {
            when (it) {
                is NetworkState.SUCCESS -> {
                    binding.rv.visibility = View.VISIBLE
                    menus.clear()
                    menus.addAll(it.data)
                    adapter.setList(menus)
                }

                is NetworkState.LOADING -> {
                    binding.rv.visibility = View.GONE
                }

                is NetworkState.FAILED -> {
                    binding.rv.visibility = View.GONE
                }

                else -> {}
            }
        }
    }

    override fun onBebasViewCreated(view: View, savedInstanceState: Bundle?) {
        val layoutManager = GridLayoutManager(requireContext(), 4)
        adapter = MenuAdapter()
        adapter.setList(menus)
        binding.rv.adapter = adapter
        binding.rv.layoutManager = layoutManager

        viewModel.getMenus()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}