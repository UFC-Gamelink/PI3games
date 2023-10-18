package com.gamelink.gamelinkapp.view.viewholder

import android.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.gamelink.gamelinkapp.databinding.RowPostsListBinding
import com.gamelink.gamelinkapp.service.listener.PostListener
import com.gamelink.gamelinkapp.service.model.PostModel

class PostViewHolder(private val itemBinding: RowPostsListBinding, val listener: PostListener) :
    RecyclerView.ViewHolder(itemBinding.root) {

    fun bindData(post: PostModel) {
        itemBinding.textPost.text = post.post

        itemBinding.icDotMenu.setOnClickListener {
            AlertDialog.Builder(itemView.context)
                .setTitle("Remover Post")
                .setMessage("Deseja apagar o post?")
                .setPositiveButton("Sim") { dialog, which ->
                    listener.onDeleteClick(post.id)
                }
                .setNeutralButton("Cancelar", null)
                .show()
        }
    }

}