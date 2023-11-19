package com.gamelink.gamelinkapp.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.gamelink.gamelinkapp.databinding.RowCommentaryListBinding
import com.gamelink.gamelinkapp.service.model.CommentaryModel

class CommentaryViewHolder(private val itemBinding: RowCommentaryListBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {
    fun bindData(commentary: CommentaryModel) {
        itemBinding.textText.text = commentary.text
    }
}