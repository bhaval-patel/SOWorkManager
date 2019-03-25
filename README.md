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

