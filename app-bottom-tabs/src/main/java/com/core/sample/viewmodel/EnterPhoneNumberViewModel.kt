package com.core.sample.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.app.api.api.CheckRegistrationRequest
import com.app.api.api.LoginRequest
import com.app.api.api.RegistrationRequest
import com.app.api.api.WumfApi
import com.core.sample.MoveToHomeScreen
import com.core.sample.ShowEnterCodeState
import com.core.sample.ShowNextButtonInProgressState
import com.core.sample.ShowSuccess
import com.core.sample.databinding.FrgEnterPhoneNumberBinding
import com.core.sample.memory.RegistrationInfo
import com.core.sample.memory.UserInfo
import com.core.sample.memory.UserInfoRepository
import com.core.sample.viewmodel.EnterPhoneNumberViewModel.State.ENTER_CODE
import com.core.sample.viewmodel.EnterPhoneNumberViewModel.State.ENTER_PHONE_NUMBER
import com.library.core.BaseViewModel
import com.library.core.di.ViewModelKey
import com.library.telegramkotlinapi.SimpleTelegramApi
import com.library.telegramkotlinapi.SimpleTelegramApi.AuthWithPhoneResult.*
import com.library.telegramkotlinapi.TelegramUser
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import retrofit2.await
import java.lang.Exception
import javax.inject.Inject

@Module
class EnterPhoneNumberModule {
    @Provides
    @IntoMap
    @ViewModelKey(EnterPhoneNumberViewModel::class)
    fun bindViewModelKey(repository: UserInfoRepository, client: Client, wumfApi: WumfApi): ViewModel
            = EnterPhoneNumberViewModel(repository, client, wumfApi)
}

class EnterPhoneNumberViewModel @Inject constructor(private var repository: UserInfoRepository,
                                                    client: Client,
                                                    private var wumfApi: WumfApi): BaseViewModel<FrgEnterPhoneNumberBinding>() {

    var phoneNumber: String = ""
    var code: String = ""
    var state: State = ENTER_PHONE_NUMBER

    private val telegramApi = SimpleTelegramApi().client(client)

    fun handleTestError() {
        handleException(Exception("Some error"))
    }

    override fun handleException(e: Exception) {

    }

    fun getSystemPhoneNumber() = repository.getPhoneNumberFromSystem()

    fun getStatusBarHeight() = repository.getStatusBarHeight()

    fun onClickNextButton() {
        when(state) {
            ENTER_PHONE_NUMBER -> {
                sendPhoneToTelegram(phoneNumber)
            }
            ENTER_CODE -> {
                sendCodeToTelegram(code)
            }
        }
    }

    fun sendCodeToTelegram(code: String) {

        fun getRegistrationInfo(userInfo: TelegramUser, hasRegistrationInWumf: Boolean): RegistrationInfo {
            val registrationInfo = RegistrationInfo()
            registrationInfo.isRegWumfChecked = true
            registrationInfo.hasRegistration = false
            registrationInfo.telegramId = userInfo.id
            registrationInfo.name = userInfo.name
            registrationInfo.photo = userInfo.photo
            return registrationInfo
        }

        fun getContactsStr(users: TdApi.Users?): String {
            users?.let {
                val usersStr = users.userIds.joinToString(",")
                return usersStr
            } ?:run {
               return ""
            }
        }

        Log.i("testr", "code=" + code)
        postEvent(ShowNextButtonInProgressState())
        state = State.PROGRESS
        startBgJob {
            var isCorrectCode: Boolean? = null

            var isRegistrationCompleted: Boolean? = null

            val sendingCodeOperation = async {
                isCorrectCode = telegramApi.checkVerificationCode(code)
                if (isCorrectCode == true) {
                    val userInfo = telegramApi.getUserInfo()

                        userInfo?.let {
                            val checkRegistration =
                                wumfApi.checkReg(CheckRegistrationRequest(userInfo.id.toString()))
                                    .await()
                            repository.setTelegramUser(
                                getRegistrationInfo(
                                    userInfo,
                                    checkRegistration.hasInDb
                                )
                            )
                            Log.i("testr", "hasInDb=" + checkRegistration.hasInDb)

                            val contacts = telegramApi.getContacts()
                            Log.i("testr", "contacts.count=" + contacts?.totalCount)

                            if (checkRegistration.hasInDb) {
                                val data = LoginRequest(userInfo.id.toString(), getContactsStr(contacts), "123")
                                val response = wumfApi.login(data).await()
                                repository.setToken(response.token)
                            } else {
                                val data = RegistrationRequest(userInfo.id.toString(), getContactsStr(contacts), "123", userInfo.name, "ua")
                                val response = wumfApi.registration(data).await()
                                repository.setToken(response.token)
                            }
                            isRegistrationCompleted = true
                        }

                }
            }
            val delayOperation = async { delay(2000) }
            sendingCodeOperation.await()
            delayOperation.await()
            if (isRegistrationCompleted == true) {
                postEvent(ShowSuccess())
                delay(1000)
                postEvent(MoveToHomeScreen())
            } else {
                //?
            }
        }
    }


    fun sendPhoneToTelegram(phoneNumber: String) {
        Log.i("testr", "phoneNumber=" + phoneNumber)
        postEvent(ShowNextButtonInProgressState())
        state = State.PROGRESS
        startBgJob {
            var sendPhoneNumberResult: SimpleTelegramApi.AuthWithPhoneResult? = null
            val sendPhoneNumberOperation = async {
                sendPhoneNumberResult = telegramApi.authWithPhone(phoneNumber)
            }
            val delayOperation = async { delay(1000) }
            delayOperation.await()
            sendPhoneNumberOperation.await()

            when (sendPhoneNumberResult) {
                SUCCESS -> {
                    showToast("onClickSendPhone success")
                }
                ERROR -> {
                    showToast("onClickSendPhone error")
                }
                ERROR_TOO_MANY_REQUESTS -> {
                    showToast("Too many requests")
                }
            }

            state = ENTER_CODE
            postEvent(ShowEnterCodeState())
        }
    }

    enum class State {
        ENTER_PHONE_NUMBER,
        ENTER_CODE,
        PROGRESS,
        ERROR_SENDING_PHONE_NUMBER,
        ERROR_SENDING_CODE
    }

}