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
import com.spaceo.workmanager.workmanager.DownLoadFIleWorkManager
import com.sunnyapps.angeleyes.utils.RunTimePermission
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity(), View.OnClickListener {
    var runtimePermission: RunTimePermission = RunTimePermission(this)
    val workManager = WorkManager.getInstance()
    var isPeriodicTaskStart: Boolean = false

    override fun onClick(p0: View?) {

        when (p0!!.id) {
            R.id.btnStartDownloadWork -> {
                runtimePermission.requestPermission(listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    object : RunTimePermission.PermissionCallback {
                        override fun onGranted() {
                            StartOneTimeWorkManager()
                        }

                        override fun onDenied() {

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

                        }

                    })

            }
        }
    }

    // Every periodic [PERIODIC_INTERVAL] interval work execute
    private fun StartPeriodicWorkManager() {
        showWorkmanagerRuning()
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
        isPeriodicTaskStart = true

        WorkManager.getInstance().getWorkInfoByIdLiveData(periodicWorkRequest.id)
            .observe(this@MainActivity, Observer {
                it?.let {
                    if (it.state == WorkInfo.State.ENQUEUED) {
                        if (!isPeriodicTaskStart) {
                            hideProgressBar()
                            Toast.makeText(this@MainActivity, getString(R.string.work_done), Toast.LENGTH_SHORT).show()
                        } else {
                            isPeriodicTaskStart = false
                        }
                    }
                }
            })
    }


    private fun showWorkmanagerRuning() {
        llProgress.visibility = View.VISIBLE
    }

    // Only one time work execute
    private fun StartOneTimeWorkManager() {
        showWorkmanagerRuning()
        val constraints = androidx.work.Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val task = OneTimeWorkRequest.Builder(DownLoadFIleWorkManager::class.java).setConstraints(constraints).build()
        workManager.enqueue(task)

        WorkManager.getInstance().getWorkInfoByIdLiveData(task.id)
            .observe(this@MainActivity, Observer {
                it?.let {
                    if (it.state.isFinished) {

                        Toast.makeText(this@MainActivity, getString(R.string.work_done), Toast.LENGTH_SHORT).show()
                        hideProgressBar()
                    }
                }
            })
    }


    private fun hideProgressBar() {
        llProgress.visibility = View.GONE
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cliecklListner()
    }

    private fun cliecklListner() {
        btnStartDownloadWork.setOnClickListener(this)
        periodaticworkmanager.setOnClickListener(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISION_REQUEST)
            runtimePermission.onRequestPermissionsResult(grantResults)

    }

}
