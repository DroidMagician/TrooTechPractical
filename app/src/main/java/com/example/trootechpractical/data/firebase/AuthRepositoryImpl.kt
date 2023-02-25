package com.example.trootechpractical.data.firebase

import android.net.Uri
import android.util.Log
import com.example.trootechpractical.data.firebase.utils.await
import com.example.trootechpractical.domain.common.Output
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): Output<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Output.success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Output.error(e.message ?: "",null)
        }
    }
    fun uploadProfileImage(path: String, result: AuthResult): Uri? {
        var uploadedImage:Uri ? = null
        val storageRef = FirebaseStorage.getInstance().getReference()
        var file = Uri.fromFile(File(path))
        val riversRef = storageRef.child("images/${file.lastPathSegment}")
        var uploadTask = riversRef.putFile(file)

// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
            Log.e("Upload","Failed")
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
            riversRef.getDownloadUrl().addOnSuccessListener { uri ->
                val uploadedImage = uri
               var myImage = uri.toString()
                Log.e("Upload Success","Download URL ${uploadedImage} download_url$myImage")

                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setPhotoUri(uploadedImage)
                    .build()

                result?.user?.updateProfile(profileUpdates)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.e("User Update Profile", "User profile updated. From Image")
                        }
                    }
            }

        }
        return  uploadedImage
    }
    override suspend fun signup(name: String, email: String, password: String,fileUri:String?): Output<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            if(fileUri !=  null)
            {
                uploadProfileImage(fileUri,result)
            }

            result?.user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())?.await()
            result?.user?.sendEmailVerification()
                ?.addOnCompleteListener(OnCompleteListener<Void?> { task ->
                    if (task.isSuccessful) {
                        // email sent
                        Log.e("Email Sent","For Verification")
                    } else {
                        // email not sent, so display message and restart the activity or do whatever you wish to do
                        Log.e("Email Not Sent","For Verification")
                        //restart this activity
                    }
                })
            Output.success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Output.error(e.message ?: "",null)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}