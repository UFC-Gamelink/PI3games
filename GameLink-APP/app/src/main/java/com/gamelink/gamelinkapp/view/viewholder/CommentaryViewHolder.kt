package com.gamelink.gamelinkapp.view.viewholder

import android.app.AlertDialog
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gamelink.gamelinkapp.R
import com.gamelink.gamelinkapp.databinding.RowCommentaryListBinding
import com.gamelink.gamelinkapp.service.constants.GameLinkConstants
import com.gamelink.gamelinkapp.service.listener.CommentaryListener
import com.gamelink.gamelinkapp.service.model.CommentaryAndProfileModel
import com.gamelink.gamelinkapp.service.model.CommentaryModel
import com.gamelink.gamelinkapp.service.repository.SecurityPreferences
import com.gamelink.gamelinkapp.utils.ImageUtils

class CommentaryViewHolder(
    private val itemBinding: RowCommentaryListBinding,
    val listener: CommentaryListener
) :
    RecyclerView.ViewHolder(itemBinding.root) {
    private val securityPreferences = SecurityPreferences(itemBinding.root.context)


    fun bindData(commentary: CommentaryAndProfileModel) {
        var liked = false
        itemBinding.textUsername.text = commentary.username
        itemBinding.textCommentary.text = commentary.commentary.text

        Glide.with(itemView).load(commentary.profile.profilePicPath)
            .into(itemBinding.imageProfile)

        itemBinding.imageLike.setOnClickListener {
            liked = if (!liked) {
                itemBinding.imageLike.setImageResource(R.drawable.ic_liked)
                true
            } else {
                itemBinding.imageLike.setImageResource(R.drawable.ic_like)
                false
            }
        }
        val userId = securityPreferences.get(GameLinkConstants.SHARED.USER_ID).toInt()

        if (commentary.commentary.userId == userId) {
            itemBinding.root.setOnLongClickListener {
                AlertDialog.Builder(itemView.context).setTitle("Remover Comentário")
                    .setMessage("Deseja apagar o comentário?").setPositiveButton("Sim") { _, _ ->
                        listener.onDeleteClick(commentary.commentary.id)
                    }.setNeutralButton("Cancelar", null).show()
                true
            }
        }
    }
}