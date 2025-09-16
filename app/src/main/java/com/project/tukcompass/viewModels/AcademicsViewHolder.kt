package com.project.tukcompass.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.tukcompass.models.TimeSlots
import com.project.tukcompass.repositories.AcademicRepo
import com.project.tukcompass.utills.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.project.tukcompass.models.ContentGroup
import com.project.tukcompass.models.CourseRequest
import com.project.tukcompass.models.CourseResponse
import com.project.tukcompass.models.CourseUnitsResponse
import com.project.tukcompass.models.TimetableResponse
import com.project.tukcompass.models.UnitContentResponse
import com.project.tukcompass.models.UnitData
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class AcademicsViewHolder @Inject constructor(private val repo: AcademicRepo): ViewModel(){

    private var _timetable = MutableLiveData<Resource<TimetableResponse>>()
    val timetable: LiveData<Resource<TimetableResponse>> = _timetable

    private var _courses = MutableLiveData<Resource< CourseResponse>>()
    val courses: LiveData<Resource<CourseResponse>> = _courses

    private var _content = MutableLiveData<Resource<UnitContentResponse>>()
    val content: LiveData<Resource<UnitContentResponse>> = _content

    private var _courseUnits = MutableLiveData<Resource<CourseUnitsResponse>>()
    val courseUnits: LiveData<Resource<CourseUnitsResponse>> = _courseUnits




    private var _unitDetails = MutableLiveData<Resource<UnitData>>()
    val unitDetails: LiveData<Resource<UnitData>> = _unitDetails

    fun getStudentsTimetable(){
        viewModelScope.launch {
            _timetable.value = Resource.Loading
            val response = repo.getStudentsTimetable()
            _timetable.postValue(response)
        }
    }
    fun fetchCourses(reqBody: CourseRequest){
        viewModelScope.launch {
            _courses.value = Resource.Loading
            val response = repo.fetchCourse(reqBody)
            _courses.postValue(response)
        }
    }
    fun getContent(offeringID: String){
        viewModelScope.launch {
            _content.value = Resource.Loading
            val response = repo.getUnitOfferingContent(offeringID)
            _content.postValue(response)
        }
    }
    fun getUnitDetails(unitID: String){
        viewModelScope.launch {
            _unitDetails.value = Resource.Loading
            val response = repo.getUnitDetails(unitID)
            _unitDetails.postValue(response)
        }
    }
    fun fetchCourseUnits(){
        viewModelScope.launch {
            _courseUnits.value = Resource.Loading
            val response = repo.fetchUserCourseUnits()
            _courseUnits.postValue(response)
        }
    }
}