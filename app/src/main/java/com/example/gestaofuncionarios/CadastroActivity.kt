package com.example.gestaofuncionarios

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CadastroActivity : AppCompatActivity() {

    private lateinit var etNome: EditText
    private lateinit var etUsuario: EditText
    private lateinit var etSenha: EditText
    private lateinit var etConfirmarSenha: EditText
    private lateinit var btCadastrarUsuario: Button
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        dbHelper = DBHelper(this)

        etNome = findViewById(R.id.etNome)
        etUsuario = findViewById(R.id.etUsuario)
        etSenha = findViewById(R.id.etSenha)
        etConfirmarSenha = findViewById(R.id.etConfirmarSenha)
        btCadastrarUsuario = findViewById(R.id.btCadastrarUsuario)

        btCadastrarUsuario.setOnClickListener {
            val nome = etNome.text.toString().trim()
            val usuario = etUsuario.text.toString().trim()
            val senha = etSenha.text.toString().trim()
            val confirmarSenha = etConfirmarSenha.text.toString().trim()

            if (nome.isEmpty() || usuario.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (senha != confirmarSenha) {
                Toast.makeText(this, "As senhas não conferem", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (dbHelper.usuarioExiste(usuario)) {
                Toast.makeText(this, "Usuário já existe", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sucesso = dbHelper.addUsuario(usuario, senha, nome)
            if (sucesso) {
                Toast.makeText(this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                // volta para login preenchendo usuário e senha
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra("prefill_user", usuario)
                    putExtra("prefill_password", senha)
                }
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Erro ao cadastrar usuário", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
