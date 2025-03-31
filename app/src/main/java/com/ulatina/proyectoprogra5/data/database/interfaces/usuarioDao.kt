import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ulatina.proyectoprogra5.data.database.model.Usuario

@Dao
interface usuarioDao {
    @Insert
    suspend fun insert(Item : Usuario)

    @Update
    suspend fun update(Item:Usuario)

    @Delete
    suspend fun  Delete(Item:Usuario)

    @Query("DELETE FROM usuarios")
    fun DropTableItems()

    @Query("select * FROM usuarios order by id")
    fun getAll(): LiveData<List<Usuario>>
}