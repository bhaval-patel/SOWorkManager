[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)

SoWorkManager
=============

WorkManager is a library used to enqueue deferrable work that is guaranteed to execute sometime after its Constraints are met. WorkManager allows observation of work status and the ability to create complex chains of work. You can read about [Constraints builder ](https://developer.android.com/reference/androidx/work/Constraints.Builder). Workmanager run in background you can get status update using LiveData.

WorkManager Execution senarios:
1) When App in Forgound
2) When App in recent list
3) If app kill then workmanager run task when app open again.

Do not forgot workmanager work with constrains, when specified contstrain met it will run tasks in above senarios.

We have developed demo where we are downloading files & store it in sdcard. Workmanager help to download in backgroud as well as when Network contrain met. Workmanager have 2 options

- One time execution : It will run only one time
- Periodic execution : It will run periodically when constrain met for task.

Here you can find steps to intigrate workmanager in your project.

STEP 1
Add dependaciy in application module gradle file (build.gradle)
```
    implementation "androidx.work:work-runtime:2.0.0-rc01"
```

STEP 2
Create workmanager instance in onCreate() of activity
```
    val workManager = WorkManager.getInstance()
```

STEP 3
Create workmanager class extends with Worker
```
    class DownLoadFileWorkManager(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
        override fun doWork(): Result {
            //TODO perform your asyc oprational task here
            /**
             * We have perform download task here on above example
             */
    
            return Result.success()
        }
    }
```

STEP 4 
Set Constraints & start(enqueue) workmanager task
  a) One Time task enqueue
```
    val constraints = androidx.work.Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
    val task = OneTimeWorkRequest.Builder(DownLoadFileWorkManager::class.java).setConstraints(constraints).build()
    workManager.enqueue(task)
```
  b)Periodic task enqueue, Recommended periodic work interval minimum time is 900000 seconds or in other words 15 minutes
```
     val periodicWorkRequest = PeriodicWorkRequest.Builder(DownLoadFileWorkManager::class.java, PERIODIC_INTERVAL, TimeUnit.MINUTES)
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()
        workManager.enqueue(periodicWorkRequest)
```

STEP 5
Get status of One time or PeriodicWork Request task status.
```
    workManager.getWorkInfoByIdLiveData(task.id)
        .observe(this@MainActivity, Observer {
            it?.let {

                if (it.state == WorkInfo.State.RUNNING) {
                    //task running, you can update UI
                }else if (it.state.isFinished) {
                    // task finished you can notify to Views
                }
            }
        })
```


![alt text](https://github.com/spaceotech/SOWorkManager/blob/master/screens/demo.gif)
![alt text](https://github.com/spaceotech/SOWorkManager/blob/master/screens/device_screenshot.png)


