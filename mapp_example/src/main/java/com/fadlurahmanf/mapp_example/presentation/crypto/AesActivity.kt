package com.fadlurahmanf.mapp_example.presentation.crypto

import com.fadlurahmanf.core_crypto.domain.repositories.CryptoAESRepository
import com.fadlurahmanf.mapp_example.databinding.ActivityAesBinding
import com.fadlurahmanf.mapp_example.presentation.BaseExampleActivity
import javax.inject.Inject

class AesActivity : BaseExampleActivity<ActivityAesBinding>(ActivityAesBinding::inflate) {

    @Inject
    lateinit var cryptoAesRepository: CryptoAESRepository

    override fun injectActivity() {
        component.inject(this)
    }

    private lateinit var key: String
    private lateinit var encryptedTextEcb: String
    private lateinit var encryptedTextCbc: String

    override fun setup() {
        binding.btnGenerateKey.onClicked {
            key = cryptoAesRepository.generateKey()
            binding.tvKey.text = "KEY: $key"
        }

        binding.btnEncryptEcb.onClicked {
            encryptedTextEcb = cryptoAesRepository.encryptECB("PLAIN_TEXT", key) ?: "-"
            binding.tvEncryptedTextEcb.text = "ENCRYPTED TEXT: $encryptedTextEcb"
        }

        binding.btnDecryptEcb.onClicked {
            binding.tvDecryptedTextEcb.text = "DECRYPTED TEXT: ${cryptoAesRepository.decryptECB(encryptedTextEcb, key)}"
        }

        binding.btnEncryptCbc.onClicked {
            encryptedTextCbc = cryptoAesRepository.encryptCBC("PLAIN_TEXT", key) ?: "-"
            binding.tvEncryptedTextCbc.text = "ENCRYPTED TEXT: $encryptedTextCbc"
        }

        binding.btnDecryptCbc.onClicked {
            binding.tvDecryptedTextCbc.text = "DECRYPTED TEXT: ${cryptoAesRepository.decryptCBC(encryptedTextCbc, key)}"
        }
    }
}