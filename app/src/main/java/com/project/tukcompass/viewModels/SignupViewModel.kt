package com.project.tukcompass.viewModels
import androidx.lifecycle.viewModelScope

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.tukcompass.models.SignupModel
import kotlinx.coroutines.launch
import com.project.tukcompass.repositories.SignupRepo
import retrofit2.Response

class SignupViewModel: ViewModel() {

    private val repo = SignupRepo()

    private val _signupResponse = MutableLiveData<Response<SignupModel>>()
    val signupResponse: LiveData<Response<SignupModel>?> = _signupResponse

    fun signup(reqBody: SignupModel) {
        viewModelScope.launch {
            val response = repo.signup(reqBody)
            _signupResponse.postValue(response)
        }
    }

}