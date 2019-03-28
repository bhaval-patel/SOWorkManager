[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)

SoWorkManager
=============

WorkManager is a library used to enqueue deferrable work that is guaranteed to execute sometime after its Constraints are met. WorkManager allows observation of work status and the ability to create complex chains of work. You can read about [Constraints builder ](https://developer.android.com/reference/androidx/work/Constraints.Builder)

Why Work manager ?
Perform any work one time,periodic and enqueue with guarantee of execution.

Step 1
Create new application with empty activity

Step 2
Add  implementation "androidx.work:work-runtime:2.0.0-rc01" depedency at app level gradle

Step 3
Add ACCESS_NETWORK_STATE and WRITE_EXTERNAL_STORAGE permision in manifiest

Step 4 
Add RunTimePermission for permision and Constant class

Step 4
Create a class for Work manager DownLoadFIleWorkManager it's extends from Work class and write code for download file in it

Step 5
In MainActivity Request for Work using OneTimeWorkRequest and PeriodicWorkRequest

