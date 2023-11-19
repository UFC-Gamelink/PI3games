package com.gamelink.gamelinkapp.view

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gamelink.gamelinkapp.databinding.CommentsBottomSheetDialogFragmentBinding
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
        binding.recyclerCommentaries.adapter = adapter

        binding.editCommentary.setOnClickListener {
            handleSave()
        }

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
            if(it) {
                viewModel.listByPost(bundle.getInt("post_id"))
                binding.editCommentary.setText("")
                Toast.makeText(context, "Comentário adicionado com sucesso", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun handleSave() {
        if(binding.editCommentary.text.toString().isNotEmpty()) {
            binding.editCommentary.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
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

}