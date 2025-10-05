package com.example.gestaofuncionarios

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "funcionarios.db"
        private const val DATABASE_VERSION = 2 // versão 2 -> adiciona tabela usuario

        // tabela funcionários
        const val TABLE_NAME = "funcionario"
        const val COL_ID = "id"
        const val COL_NOME = "nome"
        const val COL_IDADE = "idade"
        const val COL_SEXO = "sexo"
        const val COL_FUNCAO = "funcao"

        // tabela usuários (login)
        const val USER_TABLE = "usuario"
        const val USR_ID = "id"
        const val USR_LOGIN = "login"
        const val USR_SENHA = "senha"
        const val USR_NOME = "nomeFull"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // cria tabela funcionarios
        val createFuncionario = """
            CREATE TABLE IF NOT EXISTS $TABLE_NAME (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NOME TEXT,
                $COL_IDADE INTEGER,
                $COL_SEXO TEXT,
                $COL_FUNCAO TEXT
            )
        """.trimIndent()
        db.execSQL(createFuncionario)

        // cria tabela usuarios
        val createUsuario = """
            CREATE TABLE IF NOT EXISTS $USER_TABLE (
                $USR_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $USR_LOGIN TEXT UNIQUE,
                $USR_SENHA TEXT,
                $USR_NOME TEXT
            )
        """.trimIndent()
        db.execSQL(createUsuario)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            val createUsuario = """
                CREATE TABLE IF NOT EXISTS $USER_TABLE (
                    $USR_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $USR_LOGIN TEXT UNIQUE,
                    $USR_SENHA TEXT,
                    $USR_NOME TEXT
                )
            """.trimIndent()
            db.execSQL(createUsuario)
        }
    }

    // ---------- métodos funcionários ----------

    fun addFuncionario(nome: String, idade: Int, sexo: String, funcao: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_NOME, nome)
            put(COL_IDADE, idade)
            put(COL_SEXO, sexo)
            put(COL_FUNCAO, funcao)
        }
        val result = db.insert(TABLE_NAME, null, values)
        db.close()
        return result != -1L
    }

    fun getTotalFuncionarios(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_NAME", null)
        var total = 0
        if (cursor.moveToFirst()) total = cursor.getInt(0)
        cursor.close()
        db.close()
        return total
    }

    fun getTodosFuncionarios(): List<Funcionario> {
        val lista = mutableListOf<Funcionario>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        if (cursor.moveToFirst()) {
            do {
                val funcionario = Funcionario(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                    nome = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOME)),
                    idade = cursor.getInt(cursor.getColumnIndexOrThrow(COL_IDADE)),
                    sexo = cursor.getString(cursor.getColumnIndexOrThrow(COL_SEXO)),
                    funcao = cursor.getString(cursor.getColumnIndexOrThrow(COL_FUNCAO))
                )
                lista.add(funcionario)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    fun deletarFuncionario(id: Int): Boolean {
        val db = writableDatabase
        val result = db.delete(TABLE_NAME, "$COL_ID=?", arrayOf(id.toString()))
        db.close()
        return result > 0
    }

    fun atualizarFuncionario(funcionario: Funcionario): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_NOME, funcionario.nome)
            put(COL_IDADE, funcionario.idade)
            put(COL_SEXO, funcionario.sexo)
            put(COL_FUNCAO, funcionario.funcao)
        }
        val resultado = db.update(TABLE_NAME, values, "$COL_ID = ?", arrayOf(funcionario.id.toString()))
        db.close()
        return resultado > 0
    }

    // ---------- métodos usuários (login/cadastro) ----------

    fun addUsuario(login: String, senha: String, nomeFull: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(USR_LOGIN, login)
            put(USR_SENHA, senha)
            put(USR_NOME, nomeFull)
        }
        val result = db.insert(USER_TABLE, null, values)
        db.close()
        return result != -1L
    }

    // verifica login e senha
    fun verificaLogin(login: String, senha: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $USER_TABLE WHERE $USR_LOGIN = ? AND $USR_SENHA = ?",
            arrayOf(login, senha)
        )
        val valido = cursor.count > 0
        cursor.close()
        db.close()
        return valido
    }

    // checa se login já existe
    fun usuarioExiste(login: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $USER_TABLE WHERE $USR_LOGIN = ?",
            arrayOf(login)
        )
        val existe = cursor.count > 0
        cursor.close()
        db.close()
        return existe
    }

    // verifica se já existem usuários cadastrados
    fun temUsuariosCadastrados(): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $USER_TABLE", null)
        var existe = false
        if (cursor.moveToFirst()) {
            existe = cursor.getInt(0) > 0
        }
        cursor.close()
        db.close()
        return existe
    }
}
