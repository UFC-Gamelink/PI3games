package com.gamelink.gamelinkapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gamelink.gamelinkapp.R
import com.gamelink.gamelinkapp.databinding.RowGameListBinding
import com.gamelink.gamelinkapp.service.model.GameModel

class GameAdapter(var mList: List<GameModel>) : RecyclerView.Adapter<GameAdapter.GameViewHolder>() {
    private val selectedGames = mutableListOf<GameModel>()

    inner class GameViewHolder(private val itemBinding: RowGameListBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bindData(game: GameModel) {
            itemBinding.logoGame.setImageResource(game.logo)
            itemBinding.textTitleGame.text = game.title
            itemBinding.textCategories.text = game.categoriesString
            itemBinding.root.isSelected = selectedGames.contains(game)

            if (game.selected) {
                itemBinding.circleSelect.setBackgroundResource(R.drawable.circle_selected_background)
            } else {
                itemBinding.circleSelect.setBackgroundResource(R.drawable.circle_unselected_background)
            }

            itemBinding.root.setOnClickListener {
                val gameSelected = mList.find { it.id == game.id }

                if(gameSelected != null) {
                    if (selectedGames.contains(gameSelected)) {
                        selectedGames.remove(gameSelected)
                        gameSelected.selected = false
                    } else {
                        selectedGames.add(gameSelected)
                        gameSelected.selected = true
                    }
                }

                updateList(mList)

                //itemBinding.root.isSelected = selectedGames.contains(gameSelected)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = RowGameListBinding.inflate(inflater, parent, false)

        return GameViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bindData(mList[position])
    }

    fun setFilteredList(mList: List<GameModel>) {
        this.mList = mList
        notifyDataSetChanged()
    }

    fun getSelectedGames(): List<GameModel> {
        return selectedGames
    }

    private fun updateList(mList: List<GameModel>) {
        this.mList = mList
        notifyDataSetChanged()
    }
}