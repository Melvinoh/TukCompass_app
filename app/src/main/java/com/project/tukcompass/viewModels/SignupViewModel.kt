package com.project.tukcompass.viewModels
import androidx.lifecycle.viewModelScope

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.tukcompass.models.SignupReqModel
import com.project.tukcompass.models.SignupResModel
import kotlinx.coroutines.launch
import com.project.tukcompass.repositories.SignupRepo
import com.project.tukcompass.utills.Resource
import retrofit2.Response

class SignupViewModel: ViewModel() {

    private val repo = SignupRepo()


    private val _signupResponse = MutableLiveData<Resource<SignupResModel>>()
    val signupResponse: LiveData<Resource<SignupResModel>?> = _signupResponse

    fun signup(reqBody: SignupReqModel) {
        viewModelScope.launch {
            _signupResponse.value = Resource.loading
            val response = repo.signup(reqBody)
            _signupResponse.postValue(response)
        }
    }

}