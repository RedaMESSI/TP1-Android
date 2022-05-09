package com.example.tpdjebrilbenamarredamessi.user

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.example.tpdjebrilbenamarredamessi.R
import com.example.tpdjebrilbenamarredamessi.databinding.ActivityUserBinding
import com.example.tpdjebrilbenamarredamessi.databinding.FragmentTaskListBinding
import com.example.tpdjebrilbenamarredamessi.network.Api
import com.google.android.material.snackbar.Snackbar
import com.google.modernstorage.permissions.RequestAccess
import com.google.modernstorage.permissions.StoragePermissions
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.*


class UserInfoActivity : AppCompatActivity(){

    private var _binding: ActivityUserBinding? = null
    private val binding get() = _binding!!
    private val webService = Api.userWebService
    private val getPhoto = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        binding.imageView.load(photoUri) // afficher
        if (success) {
            lifecycleScope.launch {
                webService.updateAvatar(photoUri.toRequestBody())
            }

        } else {
            showMessage("Error taking picture")
        }

    }

    private val fileSystem by lazy
    private lateinit var photoUri: Uri

    private val requestCamera =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { accepted ->

            getPhoto.launch(photoUri)
        }

    val requestWriteAccess = registerForActivityResult(RequestAccess()) { accepted ->
        val camPermission = Manifest.permission.CAMERA
        val permissionStatus = checkSelfPermission(camPermission)
        val isAlreadyAccepted = permissionStatus == PackageManager.PERMISSION_GRANTED
        val isExplanationNeeded = shouldShowRequestPermissionRationale(camPermission)
        when {
            isAlreadyAccepted -> getPhoto.launch(photoUri)
            isExplanationNeeded -> showMessage("error")
            else -> requestCamera.launch(camPermission)
        }
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        binding.imageView.load(uri)
        if (uri == null) return@registerForActivityResult
        lifecycleScope.launch{
            val response=  webService.updateAvatar(uri.toRequestBody())
            binding.imageView.load(response.body()?.avatar){
                error(R.drawable.ic_launcher_background)
                placeholder(R.drawable.ic_launcher_background)
            }
        }

    }


    private fun showMessage(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
            .setAction("Open Settings") {
                val intent = Intent(
                    ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", packageName, null)
                )
                startActivity(intent)
            }
            .show()
    }

    private fun Bitmap.toRequestBody(): MultipartBody.Part {
        val tmpFile = File.createTempFile("avatar", "jpeg")
        tmpFile.outputStream().use {
            this.compress(
                Bitmap.CompressFormat.JPEG,
                100,
                it
            )
        }
        return MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "temp.jpeg",
            body = tmpFile.readBytes().toRequestBody()
        )
    }

    fun launchCameraWithPermissions() {
        requestWriteAccess.launch(
            RequestAccess.Args(
                action = StoragePermissions.Action.READ_AND_WRITE,
                types = listOf(StoragePermissions.FileType.Image),
                createdBy = StoragePermissions.CreatedBy.Self
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        _binding = ActivityUserBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        photoUri = fileSystem.createMediaStoreUri(
            filename = "picture-${UUID.randomUUID()}.jpg",
            collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            directory = "Todo",
        )!!

        binding.imageView.load("https://goo.gl/gEgYUd") {
            crossfade(true)
            transformations(CircleCropTransformation())
        }
        binding.takePictureButton.setOnClickListener{
            launchCameraWithPermissions()
        }

        binding.uploadImageButton.setOnClickListener{
            openGallery()
        }

        lifecycleScope.launch {
            val userInfo = Api.userWebService.getInfo().body()
            binding.imageView.load(userInfo?.avatar) {
                error(R.drawable.ic_launcher_background)
            }
        }

    }


    val requestReadAccess = registerForActivityResult(RequestAccess()) { hasAccess ->
        if (hasAccess) {
            galleryLauncher.launch("image/*")
        } else {
            showMessage("error")
        }
    }
    fun openGallery() {
        requestReadAccess.launch(
            RequestAccess.Args(
                action = StoragePermissions.Action.READ,
                types = listOf(StoragePermissions.FileType.Image),
                createdBy = StoragePermissions.CreatedBy.AllApps
            )
        )
    }

    private fun Uri.toRequestBody(): MultipartBody.Part {
        val fileInputStream = contentResolver.openInputStream(this)!!
        val fileBody = fileInputStream.readBytes().toRequestBody()
        return MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "temp.jpeg",
            body = fileBody
        )
    }
}

