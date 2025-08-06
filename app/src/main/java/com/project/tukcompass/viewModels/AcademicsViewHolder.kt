package com.project.tukcompass.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.tukcompass.models.TimeSlots
import com.project.tukcompass.repositories.AcademicRepo
import com.project.tukcompass.utills.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.project.tukcompass.models.CourseRequest
import com.project.tukcompass.models.CourseResponse
import com.project.tukcompass.models.TimetableResponse
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class AcademicsViewHolder @Inject constructor(private val repo: AcademicRepo): ViewModel(){

    private var _timetable = MutableLiveData<Resource<TimetableResponse>>()
    val timetable: LiveData<Resource<TimetableResponse>> = _timetable

    private var _courses = MutableLiveData<Resource< CourseResponse>>()
    val courses: LiveData<Resource<CourseResponse>> = _courses



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
}