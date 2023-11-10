package com.gamelink.gamelinkapp.view.viewholder

import android.app.AlertDialog
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gamelink.gamelinkapp.databinding.RowPostsListBinding
import com.gamelink.gamelinkapp.service.listener.PostListener
import com.gamelink.gamelinkapp.service.model.PostModel
import com.gamelink.gamelinkapp.utils.ImageUtils

class PostViewHolder(private val itemBinding: RowPostsListBinding, val listener: PostListener) :
    RecyclerView.ViewHolder(itemBinding.root) {

    fun bindData(post: PostModel) {
        itemBinding.textPost.text = post.post

        if(post.postImagePath != null) {
            Glide.with(itemView).load(post.postImagePath).into(itemBinding.imagePost)
        }

//        if(post.userId == 1) {
//            itemBinding.icDotMenu.visibility = View.GONE
//        }

        itemBinding.icDotMenu.setOnClickListener {
            AlertDialog.Builder(itemView.context)
                .setTitle("Remover Post")
                .setMessage("Deseja apagar o post?")
                .setPositiveButton("Sim") { _, _ ->
                    if(post.postImagePath != null) {
                        ImageUtils.deleteImage(post.postImagePath!!)
                    }
                    listener.onDeleteClick(post.id)
                }
                .setNeutralButton("Cancelar", null)
                .show()
        }
    }

}