package com.example.olacak.ui.fragment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI

import com.example.olacak.R
import com.example.olacak.databinding.FragmentProfileBinding
import com.example.olacak.ui.viewmodel.ProfileViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var viewModel: ProfileViewModel
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        binding.fragment = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.buttonSave.setOnClickListener {
            val userName = binding.editTextText5.text.toString()
            viewModel.saveProfileData(userName)
        }


        binding.buttonLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        imagePickerLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri = result.data?.data
                imageUri?.let {
                    viewModel.profileImageUri.value = it
                }
            }
        }

        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                openImageChooser()
            } else {

            }
        }


        binding.buttonUpload.setOnClickListener {
            openImageChooser()
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





    }



    fun onUploadButtonClick() {
        checkPermissionAndOpenImageChooser()
    }







    private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        imagePickerLauncher.launch(intent)
    }

     fun checkPermissionAndOpenImageChooser() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED -> {
                openImageChooser()
            }
            shouldShowRequestPermissionRationale(permission) -> {

                permissionLauncher.launch(permission)
            }
            else -> {
                permissionLauncher.launch(permission)
            }
        }
    }



     fun showLogoutConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { dialogInterface, _ ->
                logout()
            }
            .setNegativeButton("No") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .show()
    }

    private fun logout() {
        firebaseAuth.signOut()
        clearUserSession()
        findNavController().navigate(R.id.action_profileFragment_to_girisFragment)
    }

    private fun clearUserSession() {
        val sharedPreferences = requireActivity().getSharedPreferences("PomodoroSettings", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1001
    }
}