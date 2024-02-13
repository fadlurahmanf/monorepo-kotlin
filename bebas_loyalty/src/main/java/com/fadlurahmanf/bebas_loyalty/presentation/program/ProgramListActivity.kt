package com.fadlurahmanf.bebas_loyalty.presentation.program

import android.os.Bundle
import android.view.View
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_loyalty.data.dto.ProgramCategoryModel
import com.fadlurahmanf.bebas_loyalty.databinding.ActivityProgramListBinding
import com.fadlurahmanf.bebas_loyalty.presentation.BaseLoyaltyActivity
import com.fadlurahmanf.bebas_loyalty.presentation.program.adapter.ProgramCategoryAdapter
import javax.inject.Inject

class ProgramListActivity :
    BaseLoyaltyActivity<ActivityProgramListBinding>(ActivityProgramListBinding::inflate),
    ProgramCategoryAdapter.Callback {

    @Inject
    lateinit var viewModel: ProgramListViewModel

    override fun injectActivity() {
        component.inject(this)
    }

    private lateinit var adapter: ProgramCategoryAdapter
    private val programCategories: ArrayList<ProgramCategoryModel> = arrayListOf()

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        adapter = ProgramCategoryAdapter()
        adapter.setCallback(this)
        adapter.setList(programCategories)
        binding.rvProgramCategory.adapter = adapter

        viewModel.programCategoryState.observe(this) {
            when (it) {
                is NetworkState.FAILED -> {
                    binding.llProgramListShimmer.visibility = View.VISIBLE
                    binding.rvProgramCategory.visibility = View.GONE
                }

                is NetworkState.LOADING -> {
                    binding.llProgramListShimmer.visibility = View.VISIBLE
                    binding.rvProgramCategory.visibility = View.GONE
                }

                is NetworkState.SUCCESS -> {
                    programCategories.clear()
                    programCategories.addAll(it.data)
                    adapter.setList(programCategories)
                    if (programCategories.isNotEmpty()) {
                        val firstCategory = programCategories.first()
                        adapter.selectCategory(firstCategory.id)
                    }

                    binding.llProgramListShimmer.visibility = View.GONE
                    binding.rvProgramCategory.visibility = View.VISIBLE
                }

                else -> {

                }
            }
        }
        viewModel.getProgramListCategory()
    }

    override fun onProgramCategoryClicked(category: ProgramCategoryModel) {
        adapter.selectCategory(category.id)
    }
}