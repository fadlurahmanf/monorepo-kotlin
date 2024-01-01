package com.fadlurahmanf.bebas_transaction.presentation.ppob

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_transaction.data.dto.argument.PaymentDetailArgument
import com.fadlurahmanf.bebas_transaction.data.dto.argument.PulsaDataArgument
import com.fadlurahmanf.bebas_transaction.data.flow.PaymentDetailFlow
import com.fadlurahmanf.bebas_transaction.databinding.ActivityPaymentDetailBinding
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionActivity
import com.fadlurahmanf.bebas_transaction.presentation.ppob.pulsa_data.PulsaDataTabAdapter
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject

class PaymentDetailActivity :
    BaseTransactionActivity<ActivityPaymentDetailBinding>(ActivityPaymentDetailBinding::inflate) {

    companion object {
        const val ARGUMENT = "ARGUMENT"
        const val FLOW = "FLOW"
    }

    private lateinit var argument: PaymentDetailArgument
    private lateinit var flow: PaymentDetailFlow

    private lateinit var adapter: PulsaDataTabAdapter

    @Inject
    lateinit var viewModel: PaymentDetailViewModel


    override fun injectActivity() {
        component.inject(this)
    }

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        val stringFlow = intent.getStringExtra(FLOW)

        if (stringFlow == null) {
            showForcedBackBottomsheet(BebasException.generalRC("UNKNOWN_FLOW"))
            return
        }

        flow = enumValueOf(stringFlow)

        val p0Arg = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(ARGUMENT, PaymentDetailArgument::class.java)
        } else {
            intent.getParcelableExtra(ARGUMENT)
        }

        if (p0Arg == null) {
            showForcedBackBottomsheet(BebasException.generalRC("MISSING_ARGUMENT"))
            return
        }

        argument = p0Arg

        setupIdentityPPOB()

        if (flow == PaymentDetailFlow.PULSA_DATA) {
            adapter = PulsaDataTabAdapter(
                applicationContext,
                PulsaDataArgument(
                    providerImage = argument.additionalPulsaData?.providerImage,
                    providerName = argument.additionalPulsaData?.providerName ?: "-",
                    phoneNumber = argument.additionalPulsaData?.phoneNumber ?: "-"
                ), supportFragmentManager, lifecycle
            )

            binding.vp.adapter = adapter
            supportActionBar?.elevation = 0f

            TabLayoutMediator(binding.tabLayout, binding.vp) { tab, position ->
                tab.customView = adapter.getTabView(position)
            }.attach()

            binding.btnNext.visibility = View.GONE
        }
    }

    private fun setupIdentityPPOB() {
        when (flow) {
            PaymentDetailFlow.PULSA_DATA -> {
                binding.tabLayout.visibility = View.VISIBLE
                binding.vp.visibility = View.VISIBLE
                binding.layoutIdentityPpob.tvLabelIdentity.text = argument.labelIdentity
                binding.layoutIdentityPpob.tvIdentitySubLabel.text = argument.subLabelIdentity

                if (argument.additionalPulsaData?.providerImage != null) {
                    Glide.with(binding.layoutIdentityPpob.ivPpobLogo)
                        .load(Uri.parse(argument.additionalPulsaData?.providerImage))
                        .into(binding.layoutIdentityPpob.ivPpobLogo)
                }
            }

            PaymentDetailFlow.TELKOM_INDIHOME -> {
                binding.tabLayout.visibility = View.GONE
                binding.vp.visibility = View.GONE

                binding.layoutIdentityPpob.tvLabelIdentity.text = argument.labelIdentity
                binding.layoutIdentityPpob.tvIdentitySubLabel.text = argument.subLabelIdentity

                if (argument.additionalPulsaData?.providerImage != null) {
                    Glide.with(binding.layoutIdentityPpob.ivPpobLogo)
                        .load(Uri.parse(argument.additionalPulsaData?.providerImage))
                        .into(binding.layoutIdentityPpob.ivPpobLogo)
                    binding.layoutIdentityPpob.tvInitialAvatar.visibility = View.GONE
                    binding.layoutIdentityPpob.ivPpobLogo.visibility = View.VISIBLE
                } else {
                    binding.layoutIdentityPpob.tvInitialAvatar.visibility = View.VISIBLE
                    binding.layoutIdentityPpob.ivPpobLogo.visibility = View.GONE
                }
            }
        }
    }
}