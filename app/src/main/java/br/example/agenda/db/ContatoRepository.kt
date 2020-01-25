package br.example.agenda.db

import android.content.Context
import br.example.agenda.db.ConstantsDb.CONTATOS_DB_TABLE
import org.jetbrains.anko.db.MapRowParser
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.update
import org.jetbrains.anko.db.delete
import timber.log.Timber


class ContatoRepository(val context: Context) {

    fun findAll() : ArrayList<Contato> = context.database.use {
        val contatos = ArrayList<Contato>()

        select(CONTATOS_DB_TABLE, "id", "email", "endereco", "nome", "telefone", "site")
            .parseList(object: MapRowParser<List<Contato>> {
                override fun parseRow(columns: Map<String, Any?>): List<Contato> {
                    val contato = Contato(
                        id = columns.getValue("id").toString()?.toLong(),
                        nome = columns.getValue("nome") as String,
                        endereco = columns.getValue("endereco") as String,
                        //telefone = (columns.getValue("telefone") as String).toLong(),
                        email = columns.getValue("email") as String,
                        site = columns.getValue("site") as String)
                    contatos.add(contato)
                    return contatos
                }
            })

        contatos
    }


    fun create(contato: Contato) = context.database.use {
        insert(CONTATOS_DB_TABLE,
                "nome" to contato.nome,
                "endereco" to contato.endereco,
                "telefone" to contato.telefone,
                "email" to contato.email,
                "site" to contato.site)
    }

    fun update(contato: Contato) = context.database.use {
        val updateResult = update(CONTATOS_DB_TABLE,
            "nome" to contato.nome,
            "endereco" to contato.endereco,
            "telefone" to contato.telefone,
            "email" to contato.email,
            "site" to contato.site)
            .whereArgs("id = {id}","id" to contato.id).exec()

        Timber.d("Update result code is $updateResult")
    }


    fun delete(id: Long) = context.database.use {
        delete(CONTATOS_DB_TABLE, "id = {contatoId}", args = *arrayOf("contatoId" to id))
    }
}
