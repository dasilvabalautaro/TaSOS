package com.globalhiddenodds.tasos.tools

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import com.globalhiddenodds.tasos.R
import java.io.File
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class EnablePermissions @Inject constructor(private val permissionUtils:
                                            PermissionUtils,
                                            private val context: Context):
        ActivityCompat.OnRequestPermissionsResultCallback {

    private val imageFile = "temp.jpg"
    private val cameraPermissionsRequest = 0
    val cameraImageRequest = 1
    private val galleryPermissionsRequest = 2
    private val writeExternalStorage = 3
    private val readExternalStorage = 4
    val galleryImageRequest = 5
    private val audioSettings = 6
    private val recordAudio = 7
    private val allPermissionRequest = 8
    private val mediaProjection = 9

    fun permissionAllNeed(activity: Activity): Boolean{
        return permissionUtils.requestPermission(
                activity,
                allPermissionRequest,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.MODIFY_AUDIO_SETTINGS)
    }

    fun permissionCamera(activity: Activity): Boolean{
        return permissionUtils.requestPermission(
                activity,
                cameraPermissionsRequest,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)
    }

    fun permissionAudioSettings(activity: Activity): Boolean{
        when {
            ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.MODIFY_AUDIO_SETTINGS) !=
                    PackageManager.PERMISSION_GRANTED -> permissionUtils
                    .requestPermission(activity,
                            audioSettings,
                            Manifest.permission.MODIFY_AUDIO_SETTINGS)
            else -> return true
        }
        return false
    }

    fun permissionRecordAudio(activity: Activity): Boolean{
        when {
            ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.RECORD_AUDIO) !=
                    PackageManager.PERMISSION_GRANTED -> permissionUtils
                    .requestPermission(activity,
                            recordAudio,
                            Manifest.permission.RECORD_AUDIO)
            else -> return true
        }
        return false
    }

    fun permissionWriteExternalStorage(activity: Activity): Boolean{
        when {
            ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED -> permissionUtils
                    .requestPermission(activity,
                            writeExternalStorage,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
            else -> return true
        }
        return false
    }

    fun permissionReadExternalStorage(activity: Activity): Boolean{
        when {
            ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED -> permissionUtils
                    .requestPermission(activity,
                            readExternalStorage,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
            else -> return true
        }
        return false
    }

    fun startCamera(activity: Activity) {
        if (permissionUtils.requestPermission(
                        activity,
                        cameraPermissionsRequest,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            try {
                val photoUri = FileProvider.getUriForFile(context,
                        context.applicationContext.packageName
                                + ".provider", getCameraFile())
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                activity.startActivityForResult(intent, cameraImageRequest)

            }catch (ex: Exception){
                println("Error get image: " + ex.message)
            }


        }
    }

    fun getCameraFile(): File {
        val dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (!dir.exists()) {
            dir.mkdirs()
            println("Directory not created")
        }
        return File(dir, imageFile)
    }

    fun startGalleryChooser(activity: Activity) {
        if (permissionUtils.requestPermission(activity,
                        galleryPermissionsRequest,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            activity.startActivityForResult(Intent.createChooser(intent,
                    activity.getString(R.string.select_image)),
                    galleryImageRequest)
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        when(requestCode) {

            writeExternalStorage ->{
                if (permissionUtils
                                .permissionGranted(requestCode,
                                        writeExternalStorage,
                                        grantResults)) {
                    println("Permission write")
                }
            }
            readExternalStorage ->{
                if (permissionUtils
                                .permissionGranted(requestCode,
                                        readExternalStorage,
                                        grantResults)) {
                    println("Permission read")
                }
            }
            cameraPermissionsRequest -> if (permissionUtils
                            .permissionGranted(requestCode,
                                    cameraPermissionsRequest,
                                    grantResults)) {

                println("Permission Ok")
            }
            galleryPermissionsRequest -> if (permissionUtils
                            .permissionGranted(requestCode,
                                    galleryPermissionsRequest,
                                    grantResults)) {
                println("Permission Ok")
            }

            audioSettings -> if (permissionUtils
                            .permissionGranted(requestCode,
                                    audioSettings,
                                    grantResults)) {
                println("Permission Ok")
            }

            recordAudio -> if (permissionUtils
                            .permissionGranted(requestCode,
                                    recordAudio,
                                    grantResults)) {
                println("Permission Ok")
            }

            allPermissionRequest -> if (permissionUtils
                            .permissionGranted(requestCode,
                                    allPermissionRequest,
                                    grantResults)) {
                println("Permission Ok")
            }

            mediaProjection -> if (permissionUtils
                            .permissionGranted(requestCode,
                                    mediaProjection,
                                    grantResults)) {
                println("Permission Ok")
            }


        }

    }

}