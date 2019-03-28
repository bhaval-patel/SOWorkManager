[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)

SoWorkManager
=============

WorkManager is a library used to enqueue deferrable work that is guaranteed to execute sometime after its Constraints are met. WorkManager allows observation of work status and the ability to create complex chains of work. You can read about [Constraints builder](https://developer.android.com/reference/androidx/work/Constraints.Builder). Workmanager runs in the background you can get the status update using LiveData.

WorkManager Execution scenarios:
1) When App in Foreground
2) When App in the recent list
3) If the app kills then WorkManager run task when app open again.

Do not forget that WorkManager work with constraints, when specified constraint met it will run tasks in the above scenarios.

We have developed a demo where we are downloading files & store it in sdCard. Workmanager help to download in the background as well as when Network constraint met. Workmanager have 2 options

- One time execution: It will run only one time
- Periodic execution: It will run periodically when constraint met for the task.

Here you can find steps to integrate work manager in your project.

STEP 1
Add dependency in application module Gradle file (build.gradle)
```
    implementation "androidx.work:work-runtime:2.0.0-rc01"
```

STEP 2
Create a work manager instance in onCreate() of activity
```
    val workManager = WorkManager.getInstance()
```

STEP 3
Create a work manager class extends with Worker
```
    class DownLoadFileWorkManager(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
        override fun doWork(): Result {
            //TODO perform your async operational task here
            /**
             * We have performed download task here on above example
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
Get status of One time or periodic work Request task status.
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



