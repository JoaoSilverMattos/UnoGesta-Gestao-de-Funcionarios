package com.example.gestaofuncionarios

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegistrarActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        dbHelper = DBHelper(this)

        val etNome = findViewById<EditText>(R.id.etNome)
        val etIdade = findViewById<EditText>(R.id.etIdade)
        val etSexo = findViewById<EditText>(R.id.etSexo)
        val etFuncao = findViewById<EditText>(R.id.etFuncao)
        val btRegistrar = findViewById<Button>(R.id.btRegistrar)

        btRegistrar.setOnClickListener {
            val nome = etNome.text.toString()
            val idade = etIdade.text.toString().toIntOrNull() ?: 0
            val sexo = etSexo.text.toString()
            val funcao = etFuncao.text.toString()

            if (nome.isNotEmpty() && idade > 0 && sexo.isNotEmpty() && funcao.isNotEmpty()) {
                val success = dbHelper.addFuncionario(nome, idade, sexo, funcao)
                if (success) {
                    Toast.makeText(this, "Funcionário registrado!", Toast.LENGTH_SHORT).show()
                    finish() // volta para o menu
                } else {
                    Toast.makeText(this, "Erro ao registrar funcionário", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show()
            }
        }
    }
}