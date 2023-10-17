package com.gamelink.gamelinkapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.gamelink.gamelinkapp.R
import com.gamelink.gamelinkapp.databinding.ActivityRegisterPostBinding
import com.gamelink.gamelinkapp.service.model.PostModel
import com.gamelink.gamelinkapp.view.adapter.ItemAdapter
import com.gamelink.gamelinkapp.viewmodel.RegisterPostViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.Date

class RegisterPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterPostBinding
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: RegisterPostViewModel
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")

    private val list = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(RegisterPostViewModel::class.java)

        binding = ActivityRegisterPostBinding.inflate(layoutInflater)

        binding.buttonClose.setOnClickListener {
            finish()
        }

        for(i in 1..10) {
            list.add("item $i")
        }

        binding.textVisibilityToggle.setOnClickListener {
            showBottomSheet()
        }

        observe()

        binding.buttonPost.setOnClickListener { handlePost() }

        setContentView(binding.root)
    }

    private fun showBottomSheet () {
        val dialogView  = layoutInflater.inflate(R.layout.bottom_sheet, null)
        val dialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        dialog.setContentView(dialogView)
        recyclerView = dialogView.findViewById(R.id.recycler_view_item)
        itemAdapter = ItemAdapter(list)
        recyclerView.adapter = itemAdapter
        dialog.show()

    }

    private fun handlePost() {
        val post = PostModel().apply {
            this.post = binding.editPost.text.toString().trim()
            this.createdAt = dateFormat.format(Date());
        }

        viewModel.save(post)
    }

    private fun observe() {
        viewModel.postSave.observe(this) {
            if(it.status()) {
                Toast.makeText(applicationContext, "salvo com sucesso", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(applicationContext, it.message(), Toast.LENGTH_SHORT).show()
            }

        }
    }

}