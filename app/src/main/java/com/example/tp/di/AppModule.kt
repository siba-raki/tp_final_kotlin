package com.example.tp.di


import android.content.Context
import androidx.room.Room
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.tp.data.local.database.TaskDatabase
import com.example.tp.data.local.dao.TaskDao
import com.example.tp.data.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTaskDatabase(@ApplicationContext context: Context): TaskDatabase {
        return Room.databaseBuilder(
            context,
            TaskDatabase::class.java,
            TaskDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideTaskDao(database: TaskDatabase): TaskDao {
        return database.taskDao()
    }

    @Provides
    @Singleton
    fun provideTaskRepository(taskDao: TaskDao): TaskRepository {
        return TaskRepository(taskDao)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext context: Context) =
        PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("app_preferences") }
        )
}