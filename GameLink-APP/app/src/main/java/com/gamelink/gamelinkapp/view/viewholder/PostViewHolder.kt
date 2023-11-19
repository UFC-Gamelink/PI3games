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
) :
    RecyclerView.ViewHolder(itemBinding.root) {
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
        Glide.with(itemView)
            .load(post.userProfile.profilePicPath)
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

//            val dialog = BottomSheetDialog(itemView.context)
//
//            val view = inflater.inflate(R.layout.comment_layout_dialog, null)
//
//            val editCommentary = view.findViewById<EditText>(R.id.edit_commentary)
//
//            editCommentary.setOnClickListener {
//                myEnter(editCommentary)
//            }
//
//            val recycler: RecyclerView = view.findViewById(R.id.recycler_commentaries)
//            recycler.layoutManager = LinearLayoutManager(itemView.context)
//            val commentaryAdapter = CommentaryAdapter()
//
//            commentaryAdapter.updateCommentaries(post.commentaries)
//            recycler.adapter = commentaryAdapter
//
//            dialog.setContentView(view)
//
//            dialog.show()
//
//            Glide.with(itemView)
//                .load(post.userProfile.profilePicPath)
//                .into(view.findViewById(R.id.image_profile_commentary))
        }

        itemBinding.icDotMenu.setOnClickListener {
            AlertDialog.Builder(itemView.context)
                .setTitle("Remover Post")
                .setMessage("Deseja apagar o post?")
                .setPositiveButton("Sim") { _, _ ->
                    if (post.post.postImagePath != null) {
                        ImageUtils.deleteImage(post.post.postImagePath!!)
                    }
                    listener.onDeleteClick(post.post.id)
                }
                .setNeutralButton("Cancelar", null)
                .show()
        }
    }

    private fun myEnter(editText: EditText) {
        editText.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                Toast.makeText(itemBinding.root.context, "cliquei em enter", Toast.LENGTH_SHORT)
                    .show()
                return@setOnKeyListener true
            }
            false
        }
    }
}