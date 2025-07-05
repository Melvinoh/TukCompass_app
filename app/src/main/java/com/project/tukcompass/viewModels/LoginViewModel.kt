package com.project.tukcompass.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.tukcompass.models.LoginModels
import com.project.tukcompass.models.LoginResModel
import com.project.tukcompass.models.UserModels
import com.project.tukcompass.repositories.LoginRepo
import com.project.tukcompass.utills.Resource
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    val repo = LoginRepo()

    private val _loginResponse = MutableLiveData<Resource<LoginResModel>>()
    val loginResponse: LiveData<Resource<LoginResModel>> = _loginResponse

    fun login(reqBody: LoginModels) {
        viewModelScope.launch{
            _loginResponse.value = Resource.loading
            val response = repo.login(reqBody)
            _loginResponse.postValue(response)
        }
    }




}