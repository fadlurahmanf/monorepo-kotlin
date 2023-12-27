package com.fadlurahmanf.core_platform.domain.repositories

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricManager.BIOMETRIC_SUCCESS
import android.hardware.biometrics.BiometricPrompt
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.CancellationSignal
import android.provider.ContactsContract
import android.provider.Settings.Secure
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.fragment.app.Fragment
import com.fadlurahmanf.core_platform.data.dto.model.BebasContactModel
import androidx.biometric.BiometricPrompt as XBiometricPrompt
import com.fadlurahmanf.core_platform.external.helper.CoreBiometric
import io.reactivex.rxjava3.core.Observable
import java.util.UUID

class DeviceRepositoryImpl : DeviceRepository {

    override fun randomUUID(): String {
        return UUID.randomUUID().toString()
    }

    override fun deviceID(context: Context): String {
        return Secure.getString(context.contentResolver, Secure.ANDROID_ID)
    }

    override fun isSupportedBiometric(
        context: Context
    ): Boolean {
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            val biometricManager =
                context.getSystemService(Context.BIOMETRIC_SERVICE) as BiometricManager
            biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BIOMETRIC_SUCCESS
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
            val biometricManager =
                context.getSystemService(Context.BIOMETRIC_SERVICE) as BiometricManager
            biometricManager.canAuthenticate() == BIOMETRIC_SUCCESS
        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            val manager =
                context.getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager
            manager.hasEnrolledFingerprints()
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            val manager = FingerprintManagerCompat.from(context)
            manager.hasEnrolledFingerprints()
        } else {
            TODO("NOT IMPLEMENTED")
        }
    }

    override fun authenticateX(
        fragment: Fragment,
        titleText: String,
        descriptionText: String,
        negativeText: String,
        callback: CoreBiometric.AuthenticateXCallback,
    ) {
        val prompt = XBiometricPrompt(fragment, object :
            XBiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: XBiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                callback.onAuthenticationSuccess(result)
            }
        })
        val promptInfo = XBiometricPrompt.PromptInfo.Builder().setTitle(titleText)
            .setDescription(descriptionText)
            .setNegativeButtonText(negativeText)
            .build()
        return prompt.authenticate(promptInfo)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun authenticateP(
        context: Context,
        titleText: String,
        descriptionText: String,
        negativeText: String,
        callback: CoreBiometric.AuthenticatePCallback
    ) {
        val executor = ContextCompat.getMainExecutor(context)
        val cancellationSignal = CancellationSignal()
        cancellationSignal.setOnCancelListener {
            callback.onCancelClicked()
        }
        val prompt = BiometricPrompt.Builder(context).setTitle(titleText)
            .setDescription(descriptionText)
            .setNegativeButton(
                negativeText, executor
            ) { _, _ -> callback.onCancelClicked() }
            .build()
        return prompt.authenticate(
            cancellationSignal, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)
                    callback.onAuthenticationSuccess(result)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    callback.onAuthenticationFailed()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    super.onAuthenticationError(errorCode, errString)
                    callback.onAuthenticationError(errorCode, errString)
                }
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun authenticateM2(
        context: Context,
        callback: CoreBiometric.AuthenticateM2Callback
    ) {
        val manager =
            context.getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager
        val cancellationSignal = CancellationSignal()
        cancellationSignal.setOnCancelListener {
            callback.onCancelClicked()
        }
        return manager.authenticate(null, cancellationSignal, 0, object :
            FingerprintManager.AuthenticationCallback() {
            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                callback.onAuthenticationFailed()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                super.onAuthenticationError(errorCode, errString)
                callback.onAuthenticationError(errorCode, errString)
            }

            override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult?) {
                super.onAuthenticationSucceeded(result)
                callback.onAuthenticationSuccess(result)
            }
        }, null)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun authenticateM1(
        context: Context,
        callback: CoreBiometric.AuthenticateM1Callback
    ) {
        val manager = FingerprintManagerCompat.from(context)
        val cancellationSignal = androidx.core.os.CancellationSignal()
        cancellationSignal.setOnCancelListener {
            callback.onCancelClicked()
        }
        return manager.authenticate(null, 0, cancellationSignal, object :
            FingerprintManagerCompat.AuthenticationCallback() {
            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                callback.onAuthenticationFailed()
            }

            override fun onAuthenticationError(errMsgId: Int, errString: CharSequence?) {
                super.onAuthenticationError(errMsgId, errString)
                callback.onAuthenticationError(errMsgId, errString)
            }

            override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult?) {
                super.onAuthenticationSucceeded(result)
                callback.onAuthenticationSuccess(result)
            }
        }, null)
    }

    override fun getContacts(context: Context): Observable<List<BebasContactModel>> {
        return Observable.create<List<BebasContactModel>> { emitter ->
            val contacts = arrayListOf<BebasContactModel>()
            val contentResolver: ContentResolver = context.contentResolver
            val cursor =
                contentResolver.query(
                    ContactsContract.Contacts.CONTENT_URI,
                    null,
                    null,
                    null,
                    "display_name ASC"
                )
            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID)
                    val id =
                        cursor.getString(idIndex)
                    val hasPhoneNumberIndex =
                        cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
                    if (hasPhoneNumberIndex > 0) {
                        if (cursor.getInt(hasPhoneNumberIndex) > 0) {
                            val cursorInfo = contentResolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                arrayOf(id),
                                null
                            )
                            val person =
                                ContentUris.withAppendedId(
                                    ContactsContract.Contacts.CONTENT_URI,
                                    id.toLong()
                                )
                            while (cursorInfo != null && cursorInfo.moveToNext()) {
                                val ringIndex =
                                    cursor.getColumnIndex(ContactsContract.Contacts.CUSTOM_RINGTONE)
                                val ring =
                                    cursor.getString(ringIndex)
                                val nameIndex =
                                    cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                                val name =
                                    cursor.getString(nameIndex)
                                val mobileNumberIndex =
                                    cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                val mobileNumber =
                                    cursorInfo.getString(mobileNumberIndex)
                                // avoid gojek
                                if (name != "Gojek ✅") {
                                    contacts.add(
                                        BebasContactModel(
                                            name = name,
                                            nameHtml = name,
                                            phoneNumber = mobileNumber,
                                            phoneNumberHtml = mobileNumber
                                        )
                                    )
                                }
                            }
                            cursorInfo?.close()
                        }
                    }
                }
            }
            cursor?.close()

            emitter.onNext(contacts)
            emitter.onComplete()
        }
    }

    override fun getContactsWithIndicator(context: Context): Observable<List<BebasContactModel>> {
        return getContacts(context).map { contacts ->
            val newList = arrayListOf<BebasContactModel>()
            val listAlphabet = arrayListOf<String>()
            for (element in contacts) {
                val initial = element.name.take(1)
                if (!listAlphabet.contains(initial)) {
                    listAlphabet.add(initial)
                    newList.add(
                        BebasContactModel(
                            name = initial,
                            nameHtml = initial,
                            phoneNumber = initial,
                            phoneNumberHtml = initial,
                            type = 0
                        )
                    )
                } else {
                    newList.add(element)
                }
            }
            newList
        }
    }
}