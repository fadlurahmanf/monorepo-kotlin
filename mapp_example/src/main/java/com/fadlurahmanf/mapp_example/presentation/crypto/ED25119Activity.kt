package com.fadlurahmanf.mapp_example.presentation.crypto

import com.fadlurahmanf.core_crypto.data.dto.model.CryptoKey
import com.fadlurahmanf.core_crypto.domain.repositories.CryptoED25119Repository
import com.fadlurahmanf.mapp_example.databinding.ActivityEd25119Binding
import com.fadlurahmanf.mapp_example.presentation.BaseExampleActivity
import javax.inject.Inject

class ED25119Activity :
    BaseExampleActivity<ActivityEd25119Binding>(ActivityEd25119Binding::inflate) {

    @Inject
    lateinit var cryptoED25119Repository: CryptoED25119Repository

    override fun injectActivity() {
        component.inject(this)
    }

    private lateinit var key: CryptoKey
    private lateinit var signature: String

    override fun setup() {
        binding.btnGenerateKey.onClicked {
            key = cryptoED25119Repository.generateKey()
            binding.tvKey.text = "PRIVATE KEY: ${key.privateKey}\n\n PUBLIC KEY: ${key.publicKey}"
        }

        binding.btnGenerateSignature.onClicked {
            signature =
                cryptoED25119Repository.generateSignature("PLAIN_TEXT", key.privateKey) ?: "-"
            binding.tvSignature.text = "SIGNATURE: $signature"
        }


        binding.btnVerifySignature.onClicked {
            binding.tvVerifySignature.text = "VERIFY SIGNATURE: ${
                cryptoED25119Repository.verifySignature(
                    "PLAIN_TEXT",
                    signature,
                    key.publicKey
                )
            }"
        }
    }
}