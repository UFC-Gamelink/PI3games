package com.gamelink.gamelinkapp.view.viewholder

import android.app.AlertDialog
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
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
import java.util.Locale

class PostViewHolder(
    private val itemBinding: RowPostsListBinding,
    val listener: PostListener,
    val layoutInflater: LayoutInflater
) : RecyclerView.ViewHolder(itemBinding.root) {
    private val securityPreferences = SecurityPreferences(itemBinding.root.context)

    fun bindData(post: PostModel) {
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

        var liked: Boolean
        liked = if(post.liked) {
            itemBinding.imageLike.setImageResource(R.drawable.ic_liked)
            true
        } else {
            itemBinding.imageLike.setImageResource(R.drawable.ic_like)
            false
        }

        itemBinding.imageLike.setOnClickListener {
            listener.onLikeClick(post.id)
            liked = if (!liked) {
                itemBinding.imageLike.setImageResource(R.drawable.ic_liked)
                true
            } else {
                itemBinding.imageLike.setImageResource(R.drawable.ic_like)
                false
            }
        }

        if(post.latitude != 0.0 && post.longitude != 0.0) {
            val location = getInfoLocation(post.latitude, post.longitude)
            itemBinding.textLocation.text = location
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

    private fun getInfoLocation(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(itemBinding.root.context, Locale.getDefault())
        var addressList = mutableListOf<Address>()
        if (Build.VERSION.SDK_INT < 33) {
            addressList = geocoder.getFromLocation(latitude, longitude, 1)!!
        } else {
            geocoder.getFromLocation(latitude, longitude, 1) { addresses ->
                addressList = addresses
            }
        }


        if (addressList.size != 0) {
            val location: Address = addressList[0]

            return location.getAddressLine(0)
        }

        return ""
    }
}