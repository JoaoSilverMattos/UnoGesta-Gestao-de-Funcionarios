package com.example.gestaofuncionarios

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent

class MenuActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var tvTotal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu) // carrega o layout da tela de menu

        dbHelper = DBHelper(this)

        // Botão Voltar
        val btVoltar = findViewById<Button>(R.id.btVoltar)
        btVoltar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Botão Registrar Funcionário
        val btRegistrarFuncionario = findViewById<Button>(R.id.btRegistrarFuncionario)
        btRegistrarFuncionario.setOnClickListener {
            val intent = Intent(this, RegistrarActivity::class.java)
            startActivity(intent)
        }

        // Botão Tabela de Funcionários
        val btTabelaFuncionarios = findViewById<Button>(R.id.btTabelaFuncionarios)
        btTabelaFuncionarios.setOnClickListener {
            val intent = Intent(this, TabelaActivity::class.java)
            startActivity(intent)
        }

        // TextView para mostrar total de funcionários
        tvTotal = findViewById(R.id.tvTotal)
    }

    // Atualiza o total sempre que a tela volta a ser exibida
    override fun onResume() {
        super.onResume()
        atualizarTotal()
    }

    private fun atualizarTotal() {
        val total = dbHelper.getTotalFuncionarios()
        tvTotal.text = "Total: $total"
    }
}
