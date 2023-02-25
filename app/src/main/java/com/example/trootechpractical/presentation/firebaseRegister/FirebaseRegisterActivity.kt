package com.example.trootechpractical.presentation.firebaseRegister

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.trootechpractical.R
import com.example.trootechpractical.databinding.ActivityFirebaseRegisterBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.example.trootechpractical.domain.common.Output
import com.example.trootechpractical.presentation.base.BaseActivity
import com.example.trootechpractical.utils.Utils
import com.example.trootechpractical.presentation.firebaseLogin.FirebaseLoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * Launcher Activity (Entry point) of this application
 */

@AndroidEntryPoint
class FirebaseRegisterActivity :
    BaseActivity<FirebaseRegisterViewModel, ActivityFirebaseRegisterBinding>(
        ActivityFirebaseRegisterBinding::inflate
    ) {
    var selectedImage: String? = null
    val REQUEST_PERMISSION_SETTING = 101

    override fun initActivity() {
        listener()
        observe()
    }

    private fun listener() {
        myBinding.txtLogin.setOnClickListener {
            startActivity(FirebaseLoginActivity::class.java)
            finish()
        }
        myBinding.btnRegister.setOnClickListener {
            viewModel.signup(selectedImage)
        }
        myBinding.lilImageView.setOnClickListener {

            Dexter.withContext(this@FirebaseRegisterActivity)
                .withPermissions(Utils.getPermissionAsPerAndroidVersion())
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            startDialog()
                        } else {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", packageName, null)
                            intent.setData(uri)
                            startActivityForResult(intent, REQUEST_PERMISSION_SETTING)
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest>,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                }).check()
        }
    }

    private fun observe() {

        viewModel.errorMessage.observe(this) { result ->
            showMessage(result)
        }
        viewModel.registerResponse.observe(this) { result ->

            when (result.status) {
                Output.Status.SUCCESS -> {
                    myBinding.progressBar.visibility = View.GONE
                    result.data?.let { userObj ->
                        showMessage(getString(R.string.message_register_success))
                        startActivity(FirebaseLoginActivity::class.java)
                        finish()
                    }

                }
                Output.Status.ERROR -> {
                    myBinding.progressBar.visibility = View.GONE
                    result.message?.let {
                        showMessage(result.message)
                    }

                }
                Output.Status.LOADING -> {
                    myBinding.progressBar.visibility = View.VISIBLE
                }
                else -> {
                    //No need to handle for this
                }
            }
        }

    }


    private val takePhoto = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val imageBitmap = it.data?.extras?.get("data") as Bitmap
            myBinding.imgView.setImageBitmap(imageBitmap)
            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            val tempUri: Uri? = Utils.getImageUri(this@FirebaseRegisterActivity, imageBitmap)
            // do your thing with the obtained bitmap
            val path = tempUri?.let {
                Utils.makeFileCopyInCacheDir(
                    this@FirebaseRegisterActivity,
                    it
                )
            }
            selectedImage = path
        }
    }


    val pickImageFromGalleryForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                var selectedImageUri = result.data?.data
                // handle image from gallery
                CoroutineScope(Dispatchers.IO).launch {
                    var bitmap =
                        Glide.with(this@FirebaseRegisterActivity).asBitmap().load(selectedImageUri)
                            .submit().get()//this is synchronous approach
                    withContext(Dispatchers.Main)
                    {
                        myBinding.imgView.setImageBitmap(bitmap)
                    }
                }
                val path = result?.data?.data?.let {
                    Utils.makeFileCopyInCacheDir(
                        this@FirebaseRegisterActivity,
                        it
                    )
                }
                selectedImage = path
            }
        }

    private fun startDialog() {
        val myAlertDialog: AlertDialog.Builder = AlertDialog.Builder(
            this@FirebaseRegisterActivity
        )
        myAlertDialog.setTitle(getString(R.string.upload_picture))
        myAlertDialog.setMessage(getString(R.string.how_do_you_want_to_set_your_picture))
        myAlertDialog.setPositiveButton(
            getString(R.string.gallery)
        ) { arg0, arg1 ->

            //Select Gallery Option
            val pickIntent = Intent(Intent.ACTION_PICK)
            pickIntent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*"
            )
            pickImageFromGalleryForResult.launch(pickIntent)

        }
        myAlertDialog.setNegativeButton(
            getString(R.string.camera)
        ) { arg0, arg1 ->
            //Select Camera Option
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePhoto.launch(takePictureIntent)

        }
        myAlertDialog.show()
    }


}