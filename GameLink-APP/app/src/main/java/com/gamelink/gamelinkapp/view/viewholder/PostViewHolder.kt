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
import com.gamelink.gamelinkapp.service.model.PostProfileModel
import com.gamelink.gamelinkapp.service.repository.SecurityPreferences
import com.gamelink.gamelinkapp.utils.ImageUtils
import com.gamelink.gamelinkapp.view.CommentsBottomSheetDialogFragment

class PostViewHolder(
    private val itemBinding: RowPostsListBinding,
    val listener: PostListener,
    val layoutInflater: LayoutInflater
) : RecyclerView.ViewHolder(itemBinding.root) {
    private val securityPreferences = SecurityPreferences(itemBinding.root.context)
    private val inflater: LayoutInflater = layoutInflater

    fun bindData(post: PostProfileModel) {
        var liked = false
        itemBinding.textPost.text = post.post.post

        if (post.post.postImagePath != null) {
            Glide.with(itemView).load(post.post.postImagePath).into(itemBinding.imageviewPost)
        }

        itemBinding.textNameProfile.text = post.userProfile.name
        itemBinding.textUsernameProfile.text = "@${post.username}"
        Glide.with(itemView).load(post.userProfile.profilePicPath)
            .into(itemBinding.imageviewProfilePost)

        val userId = securityPreferences.get(GameLinkConstants.SHARED.USER_ID).toInt()

        if (post.post.userId != userId) {
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
            bundle.putInt("post_id", post.post.id)

            val commentsFragment = CommentsBottomSheetDialogFragment()
            commentsFragment.arguments = bundle

            val fragmentManager = (context as AppCompatActivity).supportFragmentManager

            commentsFragment.show(fragmentManager, commentsFragment.tag)
        }

        itemBinding.icDotMenu.setOnClickListener {
            AlertDialog.Builder(itemView.context).setTitle("Remover Post")
                .setMessage("Deseja apagar o post?").setPositiveButton("Sim") { _, _ ->
                    if (post.post.postImagePath != null) {
                        ImageUtils.deleteImage(post.post.postImagePath!!)
                    }
                    listener.onDeleteClick(post.post.id)
                }.setNeutralButton("Cancelar", null).show()
        }
    }
}