package com.gamelink.gamelinkapp.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gamelink.gamelinkapp.databinding.RowCommentaryListBinding
import com.gamelink.gamelinkapp.service.model.CommentaryAndProfileModel
import com.gamelink.gamelinkapp.service.model.CommentaryModel

class CommentaryViewHolder(private val itemBinding: RowCommentaryListBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {
    fun bindData(commentary: CommentaryAndProfileModel) {
        itemBinding.textUsername.text = commentary.username
        itemBinding.textCommentary.text = commentary.commentary.text

        Glide.with(itemView).load(commentary.profile.profilePicPath)
            .into(itemBinding.imageProfile)
    }
}