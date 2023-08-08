import android.app.Application
import androidx.room.Room

class SIECAplication: Application(){




    //Banco de dados e nome do banco;
    lateinit var  dataBase :AppDataBase


    override fun onCreate() {
        super.onCreate()
        dataBase= Room.databaseBuilder(
            applicationContext,
            AppDataBase::class.java, "BitFlow-database"
        ).build()
    }



    fun getAppDataBase(): AppDataBase{

        return dataBase



    }




}