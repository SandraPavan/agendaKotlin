package br.example.agenda.db


import android.content.Context
import android.database.sqlite.SQLiteDatabase
import br.example.agenda.db.ConstantsDb.CONTATOS_DB_NAME
import br.example.agenda.db.ConstantsDb.CONTATOS_DB_TABLE
import org.jetbrains.anko.db.ManagedSQLiteOpenHelper
import org.jetbrains.anko.db.*

class BancoDadosHelper(context: Context) :
    ManagedSQLiteOpenHelper(ctx = context ,
        name = "agenda.db",  version = 1) {

    private val scriptSQLCreate = arrayOf(
        "INSERT INTO $CONTATOS_DB_TABLE VALUES(1,'fernando.collor@gmail.com','Fernando Collor','R. Assungui, 27, Cursino, São Paulo, 04131-000, Brasil',800200300,'www.google.com.br');",
        "INSERT INTO $CONTATOS_DB_TABLE VALUES(2,'dilma@gmail.com','Dilma','R. José Cocciuffo, 90 - Cursino, São Paulo, 04121-120, Brasil',800235468,'www.uol.com.br');",
        "INSERT INTO $CONTATOS_DB_TABLE VALUES(3,'lula@gmail.com','Lula','R. José Cocciuffo, 56 - Cursino, São Paulo, 04121-120, Brasil',80023587,'www.google.com');",
        "INSERT INTO $CONTATOS_DB_TABLE VALUES(4,'maluf@gmail.com','Maluf','R. Camilo José, 48 - Cursino, São Paulo, 04125-140, Brasil',800025774,'www.uol.com.br');")

    //singleton da classe
    companion object {
        private var instance: BancoDadosHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): BancoDadosHelper {
            if (instance == null) {
                instance = BancoDadosHelper(ctx.getApplicationContext())
            }
            return instance!!
        }
    }


    override fun onCreate(db: SQLiteDatabase) {
        // Criação de tabelas
        db.createTable("$CONTATOS_DB_TABLE", true,
            "id" to INTEGER + PRIMARY_KEY + UNIQUE,
            "email" to TEXT,
            "nome" to TEXT,
            "endereco" to TEXT,
            "telefone" to INTEGER,
            //"datanascimento" to INTEGER,
            "site" to TEXT
           // "foto" to TEXT
        )

        // insere dados iniciais na tabela
        scriptSQLCreate.forEach {sql ->
            db.execSQL(sql)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Atualização do banco de dados
        db.dropTable("$CONTATOS_DB_NAME", true)
        onCreate(db)

    }

}

val Context.database: BancoDadosHelper get() = BancoDadosHelper.getInstance(getApplicationContext())

