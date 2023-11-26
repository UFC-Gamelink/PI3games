package com.gamelink.gamelinkapp.view.viewholder

import android.app.AlertDialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gamelink.gamelinkapp.R
import com.gamelink.gamelinkapp.databinding.RowPostsListBinding
import com.gamelink.gamelinkapp.service.constants.GameLinkConstants
import com.gamelink.gamelinkapp.service.listener.PostListener
import com.gamelink.gamelinkapp.service.model.PostModel
import com.gamelink.gamelinkapp.service.repository.SecurityPreferences
import com.gamelink.gamelinkapp.view.CommentsBottomSheetDialogFragment

class PostViewHolder(
    private val itemBinding: RowPostsListBinding,
    val listener: PostListener,
    val layoutInflater: LayoutInflater
) : RecyclerView.ViewHolder(itemBinding.root) {
    private val securityPreferences = SecurityPreferences(itemBinding.root.context)

    fun bindData(post: PostModel) {
        var liked = false
        itemBinding.textPost.text = post.description

        if (post.imageUrl != null) {
            Glide.with(itemView).load(post.imageUrl).into(itemBinding.imageviewPost)
        }

        itemBinding.textNameProfile.text = post.ownerName
        itemBinding.textUsernameProfile.text = "@${post.username}"
        Glide.with(itemView).load(post.userIconUrl)
            .into(itemBinding.imageviewProfilePost)

        val userId = securityPreferences.get(GameLinkConstants.SHARED.USER_ID)

        if (post.ownerId != userId) {
            itemBinding.icDotMenu.visibility = View.GONE
        }

        itemBinding.imageLike.setOnClickListener {
            liked = if (!liked) {
                itemBinding.imageLike.setImageResource(R.drawable.ic_liked)
                true
            } else {
                itemBinding.imageLike.setImageResource(R.drawable.ic_like)
                false
            }
        }

        itemBinding.imageComment.setOnClickListener {
            val context = itemBinding.root.context

            val bundle = Bundle()
            bundle.putString("post_id", post.id)

            val commentsFragment = CommentsBottomSheetDialogFragment()
            commentsFragment.arguments = bundle

            val fragmentManager = (context as AppCompatActivity).supportFragmentManager

            commentsFragment.show(fragmentManager, commentsFragment.tag)
        }

        itemBinding.icDotMenu.setOnClickListener {
            AlertDialog.Builder(itemView.context).setTitle("Remover Post")
                .setMessage("Deseja apagar o post?").setPositiveButton("Sim") { _, _ ->
                    listener.onDeleteClick(post.id)
                }.setNeutralButton("Cancelar", null).show()
        }

    }
}