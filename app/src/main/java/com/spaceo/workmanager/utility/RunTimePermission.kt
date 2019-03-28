/*
 * Copyright © 2010 - 2019 Space-O Technologies.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.spaceo.workmanager.utility

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat


class RunTimePermission(private var context: Context) {

    lateinit var permissionCallback: PermissionCallback

    interface PermissionCallback {

        fun onGranted()
        fun onDenied()
    }
    /**
     * TODO request for permission & set callback
     *
     * @param arrPermisionName List of permissions
     * @param permissionCallback provided callback to update about permissions
     */
    fun requestPermission(arrPermisionName: List<String>, permissionCallback: PermissionCallback) {
        this.permissionCallback = permissionCallback
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkAllPermisionGranted(arrPermisionName)) {
                (context as Activity).requestPermissions(arrPermisionName.toTypedArray(), PERMISION_REQUEST)
            } else {
                permissionCallback.onGranted()
            }
        } else {
            permissionCallback.onGranted()
        }
    }
    /**
     *
     * @param arrPermisionName array of permissions to check if granted or not
     */
    private fun checkAllPermisionGranted(arrPermisionName: List<String>): Boolean {
        for (i in arrPermisionName.indices) {
            if (ContextCompat.checkSelfPermission(context, arrPermisionName[i]) !== PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    /**
     * TODO requested permission result passing throgh activity filter permitted permissions & set to callback
     *
     * @param grantResults list of allowed & not granted permissions
     */
    fun onRequestPermissionsResult(grantResults: IntArray) {
        for (i in grantResults.indices) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED && null != permissionCallback) {
                permissionCallback.onGranted()

            } else {
                permissionCallback.onDenied()
                break
            }
        }
    }


}