package com.gamelink.gamelinkapp.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.gamelink.gamelinkapp.databinding.RowPostsListBinding
import com.gamelink.gamelinkapp.service.model.PostModel

class PostViewHolder(private val itemBinding: RowPostsListBinding) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bindData(post: PostModel) {
        itemBinding.textPost.text = post.post
    }

}