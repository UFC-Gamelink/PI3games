package com.gamelink.gamelinkapp.view.viewholder

import android.app.AlertDialog
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gamelink.gamelinkapp.databinding.RowPostsListBinding
import com.gamelink.gamelinkapp.service.constants.GameLinkConstants
import com.gamelink.gamelinkapp.service.listener.PostListener
import com.gamelink.gamelinkapp.service.model.PostProfileModel
import com.gamelink.gamelinkapp.service.repository.SecurityPreferences
import com.gamelink.gamelinkapp.utils.ImageUtils

class PostViewHolder(private val itemBinding: RowPostsListBinding, val listener: PostListener) :
    RecyclerView.ViewHolder(itemBinding.root) {
    private val securityPreferences = SecurityPreferences(itemBinding.root.context)

    fun bindData(post: PostProfileModel) {
        itemBinding.textPost.text = post.post.post

        if(post.post.postImagePath != null) {
            Glide.with(itemView).load(post.post.postImagePath).into(itemBinding.imageviewPost)
        }

        itemBinding.textNameProfile.text = post.userProfile.name
        itemBinding.textUsernameProfile.text = "@${post.username}"
        Glide.with(itemView)
            .load(post.userProfile.profilePicPath)
            .into(itemBinding.imageviewProfilePost)

        val userId = securityPreferences.get(GameLinkConstants.SHARED.USER_ID).toInt()

        if(post.post.userId != userId) {
            itemBinding.icDotMenu.visibility = View.GONE
        }

        itemBinding.icDotMenu.setOnClickListener {
            AlertDialog.Builder(itemView.context)
                .setTitle("Remover Post")
                .setMessage("Deseja apagar o post?")
                .setPositiveButton("Sim") { _, _ ->
                    if(post.post.postImagePath != null) {
                        ImageUtils.deleteImage(post.post.postImagePath!!)
                    }
                    listener.onDeleteClick(post.post.id)
                }
                .setNeutralButton("Cancelar", null)
                .show()
        }
    }
}