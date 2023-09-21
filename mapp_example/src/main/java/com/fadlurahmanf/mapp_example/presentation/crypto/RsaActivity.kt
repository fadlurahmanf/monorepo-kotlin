package com.fadlurahmanf.mapp_example.presentation.crypto

import com.fadlurahmanf.core_crypto.CoreCryptoComponent
import com.fadlurahmanf.core_crypto.DaggerCoreCryptoComponent
import com.fadlurahmanf.core_crypto.data.dto.model.CryptoKey
import com.fadlurahmanf.core_crypto.domain.repositories.CryptoRSARepository
import com.fadlurahmanf.mapp_example.databinding.ActivityRsaBinding
import com.fadlurahmanf.mapp_example.presentation.BaseExampleActivity
import javax.inject.Inject

class RsaActivity : BaseExampleActivity<ActivityRsaBinding>(ActivityRsaBinding::inflate) {

    @Inject
    lateinit var rsaRepository: CryptoRSARepository
    override fun injectActivity() {
        component.inject(this)
    }

    private lateinit var cryptoKey: CryptoKey
    private lateinit var encryptedText: String
    private lateinit var signatureText: String

    override fun setup() {
        binding.btnGenerateKey.onClicked {
            cryptoKey = rsaRepository.generateKey()
            binding.tvKey.text =
                "PRIVATE KEY: ${cryptoKey.privateKey} \n\nPUBLIC KEY: ${cryptoKey.publicKey}"
        }

        binding.btnEncrypt.onClicked {
            encryptedText = rsaRepository.encrypt("PLAIN_TEXT", cryptoKey.publicKey) ?: "-"
            binding.tvEncryptedText.text = "ENCRYPTED TEXT: ${encryptedText}"
        }

        binding.btnDecrypt.onClicked {
            binding.tvDecryptedText.text =
                "DECRYPTED TEXT: ${rsaRepository.decrypt(encryptedText, cryptoKey.privateKey)}"
        }

        binding.btnSignature.onClicked {
            signatureText = rsaRepository.createSignature(cryptoKey.privateKey, "PLAIN_TEXT")
            binding.tvSignatureText.text = "SIGNATURE: $signatureText"
        }

        binding.btnVerifySignature.onClicked {
            binding.tvVerifySignature.text = "IS VERIFY: ${rsaRepository.verifySignature(cryptoKey.publicKey, "PLAIN_TEXT", signatureText)}"
        }
    }
}