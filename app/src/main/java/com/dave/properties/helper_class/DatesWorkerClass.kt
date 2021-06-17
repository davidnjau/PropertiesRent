package com.dave.properties.helper_class

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class DatesWorkerClass(context: Context, workerParameters: WorkerParameters):Worker(context, workerParameters) {
    override fun doWork(): Result {

        Formatter().createNotification(applicationContext)

        return Result.success()

    }

}