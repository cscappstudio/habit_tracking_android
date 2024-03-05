package com.cscmobi.habittrackingandroid.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat


object PermissionUtil {
    const val REQUEST_CODE_PERMISSIONS = 100
    var actionShowDialog: (() -> Unit)? = null
    var listPermission = arrayListOf<String>()

    fun checkAndRequestPermissionsGranted(activity: Activity, isRequest: Boolean = true,vararg permissions: String): Boolean {
        val permissionsList: MutableList<String> = ArrayList()
        for (permission in permissions) {
            val permissionState = activity.checkSelfPermission(permission)
            if (permissionState == PackageManager.PERMISSION_DENIED) {
                permissionsList.add(permission)
            }
        }
        if (permissionsList.isNotEmpty()) {
            listPermission = permissionsList as ArrayList<String>
            if (isRequest) {
                ActivityCompat.requestPermissions(
                    activity,
                    permissionsList.toTypedArray(),
                    REQUEST_CODE_PERMISSIONS
                )
            }
            return false
        }
        return true
    }




    fun onRequestPermissionsResult(
        activity: Activity,
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
        callBack: PermissionsCallBack?
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS && grantResults.size > 0) {
            val permissionsList: MutableList<String> = ArrayList()
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionsList.add(permissions[i])
                }
            }
            if (permissionsList.isEmpty() && callBack != null) {
                callBack.permissionsGranted()
            } else {

                var showRationale = false
                for (permission in permissionsList) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            activity,
                            permission
                        )
                    ) {
                        showRationale = true
                        break
                    }
                }
                if (showRationale) {
                    actionShowDialog?.invoke()
                    showAlertDialog(activity,
                        { dialogInterface, i ->
                            checkAndRequestPermissionsGranted(
                                activity,true,
                                *permissionsList.toTypedArray()
                            )
                        }
                    ) { dialogInterface, i -> callBack?.permissionsDenied() }
                }
            }
        }
    }

    private fun showAlertDialog(
        context: Context,
        okListener: DialogInterface.OnClickListener,
        cancelListener: DialogInterface.OnClickListener
    ) {
        AlertDialog.Builder(context)
            .setMessage("Some permissions are not granted. Application may not work as expected. Do you want to grant them?")
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", cancelListener)
            .create()
            .show()
    }

    interface PermissionsCallBack {
        fun permissionsGranted()
        fun permissionsDenied()
    }
}