package com.example.to_docompose

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
//trigger hilt code generations including base class for our application that will serve
// as the application level dependency controller
class ToDoApplication : Application()