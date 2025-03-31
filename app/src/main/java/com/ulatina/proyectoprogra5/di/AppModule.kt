package com.ulatina.proyectoprogra5.di

import RutinasDao
import android.content.Context
import androidx.lifecycle.LiveData
import com.ulatina.proyectoprogra5.data.database.AppDataBase
import com.ulatina.proyectoprogra5.data.database.model.Rutina
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule
{
        @Provides
        @Singleton
        fun provideAppDataBase(@ApplicationContext context: Context): AppDataBase {
            return AppDataBase.getDatabase(context)
        }
        @Provides
        @Singleton
        fun providesItemDao(AppDataBase: AppDataBase): RutinasDao {
            return AppDataBase.RutinasDao()
        }
        @Provides
        @Singleton
        fun providesItemRep(itemDao: RutinasDao): LiveData<List<Rutina>> {
            return itemDao.getAll()
        }
}