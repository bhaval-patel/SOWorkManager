
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
package com.spaceo.workmanager
import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.*
import com.spaceo.workmanager.utility.PERIODIC_INTERVAL
import com.spaceo.workmanager.utility.PERMISION_REQUEST
import com.spaceo.workmanager.utility.RunTimePermission
import com.spaceo.workmanager.workmanager.DownLoadFIleWorkManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity(), View.OnClickListener {
    /**
     * Runtime permissions object init to check storage persmissions
     */
    var runtimePermission: RunTimePermission = RunTimePermission(this)

    /**
     *  Workmanager global instance to enqueue tasks & get update
     */
    val workManager = WorkManager.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //init clicklistener for both buttons
        btnStartDownloadWork.setOnClickListener(this@MainActivity)
        periodaticworkmanager.setOnClickListener(this@MainActivity)
    }

    /**
     * TODO When use clicl on buttons it will call below methods
     *
     * @param view clicked item view(or id)
     */
    override fun onClick(view: View) {

        when (view.id) {
            R.id.btnStartDownloadWork -> {
                runtimePermission.requestPermission(listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    object : RunTimePermission.PermissionCallback {
                        override fun onGranted() {

                            StartOneTimeWorkManager()
                        }

                        override fun onDenied() {
                            //show message if not allow storage permission
                        }
                    })
            }

            R.id.periodaticworkmanager -> {
                runtimePermission.requestPermission(listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    object : RunTimePermission.PermissionCallback {
                        override fun onGranted() {

                            StartPeriodicWorkManager()
                        }

                        override fun onDenied() {
                            //show message if not allow storage permission
                        }

                    })

            }
        }
    }


    private fun StartOneTimeWorkManager() {

        val constraints = androidx.work.Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val task = OneTimeWorkRequest.Builder(DownLoadFIleWorkManager::class.java).setConstraints(constraints).build()
        workManager.enqueue(task)

        //
        workManager.getWorkInfoByIdLiveData(task.id)
            .observe(this@MainActivity, Observer {
                it?.let {

                    if (it.state == WorkInfo.State.RUNNING) {
                        loaderShow(true)

                    }else
                    if (it.state.isFinished) {

                        Toast.makeText(this@MainActivity, getString(R.string.work_done), Toast.LENGTH_SHORT).show()
                        loaderShow(false)
                    }
                }
            })
    }

    // Every periodic [PERIODIC_INTERVAL] interval work execute
    private fun StartPeriodicWorkManager() {
        loaderShow(true)
        val periodicWorkRequest = PeriodicWorkRequest.Builder(
            DownLoadFIleWorkManager::class.java,
            PERIODIC_INTERVAL,
            TimeUnit.MINUTES
        ).setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        ).build()

        workManager.enqueue(periodicWorkRequest)


        workManager.getWorkInfoByIdLiveData(periodicWorkRequest.id)
            .observe(this@MainActivity, Observer {
                it?.let {
                    if (it.state == WorkInfo.State.ENQUEUED) {

                        loaderShow(false)
                        Toast.makeText(this@MainActivity, getString(R.string.work_done), Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }

    /**
     * Loader visibility
     */
    private fun loaderShow(flag: Boolean) {
        when (flag) {
            true -> llProgress.visibility = View.VISIBLE
            false -> llProgress.visibility = View.GONE
        }
    }

    /**
     * Request permission result pass to RuntimePermission.kt
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISION_REQUEST)
            runtimePermission.onRequestPermissionsResult(grantResults)

    }

}
