package com.project.tukcompass.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.tukcompass.models.ChatResponse
import com.project.tukcompass.models.ContactsRes
import com.project.tukcompass.models.LoginModels
import com.project.tukcompass.models.LoginResModel
import com.project.tukcompass.models.SignupReqModel
import com.project.tukcompass.models.SignupResModel
import com.project.tukcompass.models.UserModel
import com.project.tukcompass.repositories.AuthRepo
import com.project.tukcompass.utills.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repo: AuthRepo) : ViewModel() {


    private val _signupResponse = MutableLiveData<Resource<SignupResModel>>()
    val signupResponse: LiveData<Resource<SignupResModel>> = _signupResponse
    private val _loginResponse = MutableLiveData<Resource<LoginResModel>>()
    val loginResponse: LiveData<Resource<LoginResModel>> = _loginResponse
    private val _userProfile = MutableLiveData<MutableList<UserModel?>>()
    val userProfile: MutableLiveData<MutableList<UserModel?>> = _userProfile


    private var _contacts = MutableLiveData<Resource<ContactsRes>>()
    val contacts: LiveData<Resource<ContactsRes>> = _contacts

    fun login(reqBody: LoginModels) {
        viewModelScope.launch{
            _loginResponse.value = Resource.Loading
            val response = repo.login(reqBody)
            _loginResponse.postValue(response)
        }
    }
    fun signup(reqBody: SignupReqModel) {
        viewModelScope.launch {
            _signupResponse.value = Resource.Loading
            val response = repo.signup(reqBody)
            _signupResponse.postValue(response)
        }
    }

    fun getUserContacts(){
        viewModelScope.launch {
            _contacts.value = Resource.Loading
            val response = repo.getUserContacts()
            _contacts.postValue(response)
        }
    }


}