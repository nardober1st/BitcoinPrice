import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1)
abstract class AppDataBase : RoomDatabase() {}