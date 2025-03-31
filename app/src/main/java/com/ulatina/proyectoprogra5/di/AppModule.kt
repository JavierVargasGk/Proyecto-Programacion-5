package com.ulatina.proyectoprogra5.di

import usuarioDao
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
        fun providesItemDao(AppDataBase: AppDataBase): usuarioDao {
            return AppDataBase.UsuarioDao()
        }
        @Provides
        @Singleton
        fun providesItemRep(itemDao: usuarioDao): LiveData<List<Usuario>> {
            return itemDao.getAll()
        }
}