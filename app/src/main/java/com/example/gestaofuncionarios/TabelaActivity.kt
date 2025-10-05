package com.example.gestaofuncionarios

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TabelaActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var rvFuncionarios: RecyclerView
    private lateinit var funcionarioAdapter: FuncionarioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tabela)

        dbHelper = DBHelper(this)

        rvFuncionarios = findViewById(R.id.rvFuncionarios)
        rvFuncionarios.layoutManager = LinearLayoutManager(this)

        // Dados de teste se banco estiver vazio
        if (dbHelper.getTotalFuncionarios() == 0) {
            dbHelper.addFuncionario("João", 30, "Masculino", "Supervisor")
            dbHelper.addFuncionario("Maria", 25, "Feminino", "Auxiliar")
        }

        carregarFuncionarios()
    }

    private fun carregarFuncionarios() {
        val listaFuncionarios = dbHelper.getTodosFuncionarios().toMutableList()
        Log.d("TabelaActivity", "Total de funcionários: ${listaFuncionarios.size}")

        funcionarioAdapter = FuncionarioAdapter(listaFuncionarios) { funcionario, position ->
            // Menu de opções
            val options = arrayOf("Editar", "Remover")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Escolha uma ação")
            builder.setItems(options) { dialog, which ->
                when (which) {
                    0 -> abrirTelaEdicao(funcionario, position) // Editar
                    1 -> {
                        val sucesso = dbHelper.deletarFuncionario(funcionario.id) // Remover
                        if (sucesso) {
                            funcionarioAdapter.removerItem(position)
                        }
                    }
                }
            }
            builder.show()
        }

        rvFuncionarios.adapter = funcionarioAdapter
    }

    private fun abrirTelaEdicao(funcionario: Funcionario, position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Editar funcionário")

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 40, 50, 10)

        val etNome = EditText(this)
        etNome.hint = "Nome"
        etNome.setText(funcionario.nome)
        layout.addView(etNome)

        val etIdade = EditText(this)
        etIdade.hint = "Idade"
        etIdade.inputType = InputType.TYPE_CLASS_NUMBER
        etIdade.setText(funcionario.idade.toString())
        layout.addView(etIdade)

        val etSexo = EditText(this)
        etSexo.hint = "Sexo"
        etSexo.setText(funcionario.sexo)
        layout.addView(etSexo)

        val etFuncao = EditText(this)
        etFuncao.hint = "Função"
        etFuncao.setText(funcionario.funcao)
        layout.addView(etFuncao)

        builder.setView(layout)

        builder.setPositiveButton("Salvar") { dialog, _ ->
            val atualizado = Funcionario(
                id = funcionario.id,
                nome = etNome.text.toString(),
                idade = etIdade.text.toString().toIntOrNull() ?: funcionario.idade,
                sexo = etSexo.text.toString(),
                funcao = etFuncao.text.toString()
            )
            dbHelper.atualizarFuncionario(atualizado)
            funcionarioAdapter.atualizarItem(position, atualizado)
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }
}
