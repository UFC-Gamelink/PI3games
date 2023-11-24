package com.gamelink.gamelinkapp.view.createProfile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gamelink.gamelinkapp.R
import com.gamelink.gamelinkapp.databinding.ActivityCreateProfileStep5Binding
import com.gamelink.gamelinkapp.service.listener.CategoryGameListener
import com.gamelink.gamelinkapp.service.model.CategoryGameModel
import com.gamelink.gamelinkapp.service.model.GameModel
import com.gamelink.gamelinkapp.service.model.ProfileModel
import com.gamelink.gamelinkapp.view.MainActivity
import com.gamelink.gamelinkapp.view.adapter.CategoryGameAdapter
import com.gamelink.gamelinkapp.view.adapter.GameAdapter
import com.gamelink.gamelinkapp.viewmodel.SaveProfileViewModel

class CreateProfileStep5Activity : AppCompatActivity(), View.OnClickListener, SearchView.OnQueryTextListener {
    private lateinit var viewModel: SaveProfileViewModel
    private lateinit var binding: ActivityCreateProfileStep5Binding
    private lateinit var bundle: Bundle
    private var mList = ArrayList<GameModel>()
    private var categoriesGameList = ArrayList<CategoryGameModel>()
    private lateinit var gameAdapter: GameAdapter
    private lateinit var categoryGameAdapter: CategoryGameAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateProfileStep5Binding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(SaveProfileViewModel::class.java)

        bundle = intent.extras!!

        binding.recyclerGames.layoutManager = LinearLayoutManager(this)
        binding.recyclerCategoryGame.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        addDataToList()
        gameAdapter = GameAdapter(mList)
        binding.recyclerGames.adapter = gameAdapter
        binding.searchGame.setOnQueryTextListener(this)

        val listener = object : CategoryGameListener {
            override fun onSelectClick() {

            }

            override fun onUnselectClick() {
                TODO("Not yet implemented")
            }

        }
        addDataToCategoriesGameList()
        categoryGameAdapter = CategoryGameAdapter(categoriesGameList)
        binding.recyclerCategoryGame.adapter = categoryGameAdapter
        categoryGameAdapter.attachListener(listener)

        binding.buttonNext.setOnClickListener(this)

        observe()
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.button_next -> handleRegister()
        }
    }

    private fun handleRegister() {
        val name: String = bundle.getString("name") ?: ""
        val profilePicturePath: String = bundle.getString("profile_picture_path") ?: ""
        val profileBannerPath: String = bundle.getString("profile_banner_path") ?: ""
        val bio: String = bundle.getString("bio_profile") ?: ""
        val birthday: String = bundle.getString("birthday") ?: ""
        val showBirthday: Boolean = bundle.getBoolean("show_birthday")

        val profile = ProfileModel().apply {
            this.name = name
            this.profilePicPath = profilePicturePath
            this.bannerPicPath = profileBannerPath
            this.bio = bio
            this.birthday = birthday
            this.showBirthday = showBirthday
        }

        viewModel.save(profile)
    }

    private fun observe(){
        viewModel.profileSave.observe(this) {
            if(it.status()) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, it.message(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addDataToList() {
        mList.add(GameModel(1, "Valorant", R.drawable.valorant_icon, listOf("FPS", "MultiPlayer")))
        mList.add(GameModel(2, "Counter Strike: Global Ofensive", R.drawable.valorant_icon, listOf("FPS", "MultiPlayer")))
        mList.add(GameModel(3, "Overwatch", R.drawable.valorant_icon, listOf("FPS", "MultiPlayer")))
        mList.add(GameModel(4, "Warface", R.drawable.valorant_icon, listOf("FPS", "MultiPlayer")))
    }
    private fun addDataToCategoriesGameList(){
        categoriesGameList.add(CategoryGameModel("FPS"))
        categoriesGameList.add(CategoryGameModel("MOBA"))
        categoriesGameList.add(CategoryGameModel("Indie"))
        categoriesGameList.add(CategoryGameModel("Esportes"))
        categoriesGameList.add(CategoryGameModel("Party Games"))
    }

    private fun filterList(query: String?) {
        if(query != null) {
            val filteredList = ArrayList<GameModel>()
            for(i in mList) {
                if(i.title.lowercase().contains(query)){
                    filteredList.add(i)
                }
            }

            if(filteredList.isEmpty()) {
                Toast.makeText(this, "Jogo Não encontrado", Toast.LENGTH_SHORT).show()
            }  else {
                gameAdapter.setFilteredList(filteredList)
            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        filterList(newText)
        return true
    }
}
