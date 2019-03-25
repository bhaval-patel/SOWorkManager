package com.spaceo.workmanager.workmanager

import android.content.Context

import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.spaceo.workmanager.utility.PATHFILE
import com.spaceo.workmanager.utility.URLFILE
import java.io.BufferedInputStream
import java.io.FileOutputStream
import java.net.URL


class DownLoadFIleWorkManager(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): ListenableWorker.Result {
        try {
            val url = URL(URLFILE)
            val conection = url.openConnection()
            conection.connect()
            // getting file length

            // input stream to read file - with 8k buffer
            val input = BufferedInputStream(url.openStream(), 8192)

            // Output stream to write file
            val output = FileOutputStream(PATHFILE)

            val data = ByteArray(1024)

            var count: Int? = 0

            while ({ count = input.read(data);count }() != -1) {
                output.write(data, 0, count!!)
            }

            // flushing output
            output.flush()

            // closing streams
            output.close()
            input.close()

        } catch (e: Exception) {
            return Result.retry()
        }

        return Result.success()
    }


}