package com.ulatina.proyectoprogra5.di

import com.ulatina.proyectoprogra5.data.database.interfaces.UsuarioDao
import android.content.Context
import androidx.lifecycle.LiveData
import com.ulatina.proyectoprogra5.data.database.AppDataBase
import com.ulatina.proyectoprogra5.data.database.model.Usuario
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
        fun providesItemDao(appDataBase: AppDataBase): UsuarioDao {
            return appDataBase.UsuarioDao()
        }
        @Provides
        @Singleton
        fun providesItemRep(itemDao: UsuarioDao): LiveData<List<Usuario>> {
            return itemDao.getAll()
        }
}