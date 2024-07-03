package com.example.olacak.ui.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(@ApplicationContext private val context: Context) : ViewModel() {
    val userName = MutableLiveData<String>()
    val profileImageUri = MutableLiveData<Uri>()
    private val sharedPreferences = context.getSharedPreferences("PomodoroSettings", Context.MODE_PRIVATE)


    init {
        loadProfileData()
    }

    private fun loadProfileData() {
        userName.value = sharedPreferences.getString("user_name", "")
        val imageUriString = sharedPreferences.getString("profile_image_uri", null)
        profileImageUri.value = imageUriString?.let { Uri.parse(it) }
    }


    fun saveProfileData(name: String) {
        userName.value = name
        sharedPreferences.edit().putString("user_name", name).apply()

        profileImageUri.value?.let { uri ->
            sharedPreferences.edit().putString("profile_image_uri", uri.toString()).apply()
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("imageUri")
        fun loadImage(imageView: ImageView, uri: MutableLiveData<Uri>?) {
            uri?.value?.let { imageView.setImageURI(it) }
        }
    }

    fun onUploadButtonClick() {

    }

    fun onLogoutButtonClick() {


    }
}
