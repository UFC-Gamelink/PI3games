package com.gamelink.gamelinkapp.view

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.gamelink.gamelinkapp.databinding.CommentsBottomSheetDialogFragmentBinding
import com.gamelink.gamelinkapp.service.listener.CommentaryListener
import com.gamelink.gamelinkapp.service.model.CommentaryModel
import com.gamelink.gamelinkapp.view.adapter.CommentaryAdapter
import com.gamelink.gamelinkapp.viewmodel.CommentsBottomSheetDialogViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CommentsBottomSheetDialogFragment : BottomSheetDialogFragment() {
    private lateinit var viewModel: CommentsBottomSheetDialogViewModel
    private var _binding: CommentsBottomSheetDialogFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var bundle: Bundle
    private val adapter = CommentaryAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CommentsBottomSheetDialogFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(CommentsBottomSheetDialogViewModel::class.java)

        binding.recyclerCommentaries.layoutManager = LinearLayoutManager(context)

        adapter.attachListener(object : CommentaryListener {
            override fun onDeleteClick(commentaryId: Int) {
                viewModel.delete(commentaryId)
            }

        })

        binding.recyclerCommentaries.adapter = adapter

        binding.editCommentary.setOnClickListener {
            handleSave()
        }

        viewModel.getProfile()

        observe()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        bundle = arguments as Bundle

        val postId = bundle.getInt("post_id")

        viewModel.listByPost(postId)
    }

    private fun observe() {
        viewModel.comments.observe(viewLifecycleOwner) {
            adapter.updateCommentaries(it)
        }

        viewModel.commentarySave.observe(viewLifecycleOwner) {
            if (it) {
                viewModel.listByPost(bundle.getInt("post_id"))
                binding.editCommentary.setText("")
                Toast.makeText(context, "Comentário adicionado com sucesso", Toast.LENGTH_SHORT)
                    .show()

            }
        }

        viewModel.profilePic.observe(viewLifecycleOwner) {
            Glide.with(this).load(it).into(binding.imageProfileCommentary)
        }

        viewModel.deleteCommentary.observe(viewLifecycleOwner) {
            if (it.status()) {
                viewModel.listByPost(bundle.getInt("post_id"))
                Toast.makeText(context, "Comentário apagado com sucesso", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleSave() {

        binding.editCommentary.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                if(binding.editCommentary.text.toString().isEmpty()) {
                    return@setOnKeyListener false
                }

                viewModel.save(CommentaryModel().apply {
                    this.postId = bundle.getInt("post_id")
                    this.text = binding.editCommentary.text.toString()
                })

                return@setOnKeyListener true
            }
            false
        }
    }

}