import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ulatina.proyectoprogra5.data.database.model.Rutina

@Dao
interface RutinasDao {
    @Insert
    suspend fun insert(Item : Rutina)

    @Update
    suspend fun update(Item:Rutina)

    @Delete
    suspend fun  Delete(Item:Rutina)

    @Query("DELETE FROM rutinas")
    fun DropTableItems()

    @Query("select * FROM rutinas order by id")
    fun getAll(): LiveData<List<Rutina>>
}