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
package com.spaceo.workmanager.workmanager

import android.content.Context

import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.spaceo.workmanager.utility.URLFILE
import java.io.BufferedInputStream
import java.io.FileOutputStream
import java.net.URL


class DownLoadFileWorkManager(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    /**
     * Workmanager worker thread which do processing
     * in background, so it will not impact to main thread or UI
     *
     */
    override fun doWork(): ListenableWorker.Result {
        try {
            val url = URL(URLFILE)
            val conection = url.openConnection()
            conection.connect()
            // getting file length

            // input stream to read file - with 8k buffer
            val input = BufferedInputStream(url.openStream(), 8192)

            // Output stream to write file
            val output = FileOutputStream("/sdcard/myfile_${System.currentTimeMillis()}.mp4")

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