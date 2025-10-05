package com.example.gestaofuncionarios

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etUsuario: EditText
    private lateinit var etSenha: EditText
    private lateinit var btLogin: Button
    private lateinit var btCadastrar: Button
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // layout do login

        supportActionBar?.hide()

        dbHelper = DBHelper(this)

        etUsuario = findViewById(R.id.editUsername)
        etSenha = findViewById(R.id.editPassword)
        btLogin = findViewById(R.id.btLogin)
        btCadastrar = findViewById(R.id.btCadastrar)

        // Botão cadastrar -> abre tela de cadastro
        btCadastrar.setOnClickListener {
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
        }

        // Botão login -> valida antes de permitir
        btLogin.setOnClickListener {
            val usuario = etUsuario.text.toString().trim()
            val senha = etSenha.text.toString().trim()

            if (usuario.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha usuário e senha", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Primeiro verifica se existe usuário cadastrado no banco
            if (!dbHelper.temUsuariosCadastrados()) {
                Toast.makeText(this, "Nenhum usuário cadastrado. Cadastre-se primeiro.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Depois valida se usuário e senha estão corretos
            val valido = dbHelper.verificaLogin(usuario, senha)
            if (valido) {
                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Usuário ou senha inválidos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
