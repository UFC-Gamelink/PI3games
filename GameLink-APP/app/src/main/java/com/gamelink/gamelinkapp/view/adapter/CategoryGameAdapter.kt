package com.gamelink.gamelinkapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gamelink.gamelinkapp.R
import com.gamelink.gamelinkapp.databinding.RowCategoryGameListBinding
import com.gamelink.gamelinkapp.service.listener.CategoryGameListener
import com.gamelink.gamelinkapp.service.model.CategoryGameModel

class CategoryGameAdapter(private var mList: List<CategoryGameModel>) :
    RecyclerView.Adapter<CategoryGameAdapter.GameViewHolder>() {
    private lateinit var listener: CategoryGameListener

    inner class GameViewHolder(
        private val itemBinding: RowCategoryGameListBinding,
        val listener: CategoryGameListener
    ) : RecyclerView.ViewHolder(itemBinding.root) {


        fun bindData(categoryGameModel: CategoryGameModel) {
            itemBinding.textTitleCategory.text = categoryGameModel.title

            itemBinding.root.setOnClickListener {
                if(categoryGameModel.selected) {
                    listener.onSelectClick()
                } else {
                    listener.onUnselectClick()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = RowCategoryGameListBinding.inflate(inflater, parent, false)

        return GameViewHolder(itemBinding, listener)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bindData(mList[position])
    }

    fun attachListener(categoryGameListener: CategoryGameListener) {
        listener = categoryGameListener
    }

}