package com.example.trootechpractical.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File

object FileDownloader {

     fun downloadFile(uri: Uri, fileName: String,context: Activity): Long {
        var downloadReference: Long = 0
        var downloadManager = context.getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager
        try {
            val request = DownloadManager.Request(uri)

            //Setting title of request
            request.setTitle(fileName)

            //Setting description of request
            request.setDescription("Your file is downloading")

            //set notification when download completed
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

            //Set the local destination for the downloaded file to a path within the application's external files directory
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            request.allowScanningByMediaScanner()

            //Enqueue download and save the referenceId
            downloadReference = downloadManager.enqueue(request)
        } catch (e: IllegalArgumentException) {
            Log.e("TAG", "Line no: 455,Method: downloadFile: Download link is broken")
        }
        return downloadReference
    }

    @JvmStatic
    fun getFileDirectory(context: Context): File? {
        val fileDir = context.getExternalFilesDir(null)
        if (fileDir != null) {
            if (fileDir.isDirectory) {
                return fileDir
            } else {
                val b = fileDir.mkdir()
                if (b) {
                    return fileDir
                }
            }
        }
        return fileDir
    }

    interface DownloadListener {
        fun onSuccess(path: String?)
        fun onFailed()
    }
}