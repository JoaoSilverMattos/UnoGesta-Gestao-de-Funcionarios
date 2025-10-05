package com.example.gestaofuncionarios

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FuncionarioAdapter(
    private val lista: MutableList<Funcionario>,
    private val onItemLongClick: (Funcionario, Int) -> Unit
) : RecyclerView.Adapter<FuncionarioAdapter.FuncionarioViewHolder>() {

    class FuncionarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNome: TextView = itemView.findViewById(R.id.tvNomeItem)
        val tvIdade: TextView = itemView.findViewById(R.id.tvIdadeItem)
        val tvSexo: TextView = itemView.findViewById(R.id.tvSexoItem)
        val tvFuncao: TextView = itemView.findViewById(R.id.tvFuncaoItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FuncionarioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_funcionario, parent, false)
        return FuncionarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: FuncionarioViewHolder, position: Int) {
        val funcionario = lista[position]
        holder.tvNome.text = funcionario.nome
        holder.tvIdade.text = funcionario.idade.toString()
        holder.tvSexo.text = funcionario.sexo
        holder.tvFuncao.text = funcionario.funcao

        holder.itemView.setOnLongClickListener {
            onItemLongClick(funcionario, position)
            true
        }
    }

    override fun getItemCount() = lista.size

    fun removerItem(position: Int) {
        lista.removeAt(position)
        notifyItemRemoved(position)
    }

    fun atualizarItem(position: Int, funcionario: Funcionario) {
        lista[position] = funcionario
        notifyItemChanged(position)
    }
}
