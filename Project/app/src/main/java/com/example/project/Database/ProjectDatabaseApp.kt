package com.example.project.Database

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob


class ProjectDatabaseApp: Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())


    val database by lazy { ProjectDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { UserRepository(database.UserDao()) }

}