package com.gamelink.gamelinkapp.service.mocks

import com.gamelink.gamelinkapp.service.model.PostModel
import com.gamelink.gamelinkapp.service.model.PostProfileModel
import com.gamelink.gamelinkapp.service.model.ProfileModel
import java.text.SimpleDateFormat
import java.util.Date

class MockPosts {
    fun getRecommendedPosts(): List<PostProfileModel> {
        val listPosts: MutableList<PostProfileModel> = mutableListOf()

        for (i in 0..19) {
            val userProfile = ProfileModel()
            userProfile.id = "${i + 99}"
            userProfile.name = generateRandomName()[i]
            userProfile.username = generateRandomUsername()[i]
            userProfile.owner = "${i + 95}"
            userProfile.profilePicPath = "https://media.istockphoto.com/id/1305224036/pt/foto/latin-man-gaming-on-his-pc-during-a-live-stream.jpg?s=612x612&w=0&k=20&c=d7CHJY8R_mdRONaA7c62pdAD7308HkdZIL-Ne5t6T3w="

            val postModel = PostModel()
            postModel.id = i + 95
            postModel.post = generateRandomPost()[i]
            postModel.visibility = 1
            postModel.createdAt = SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Date())
            postModel.userId = i + 99

            listPosts.add(PostProfileModel(postModel, userProfile, userProfile.username))
        }

        return listPosts
    }

    private fun generateRandomName(): List<String> {
        return listOf(
            "Alice", "Bob", "Charlie", "David", "Emma",
            "Frank", "Grace", "Henry", "Ivy", "Jack",
            "Katherine", "Liam", "Mia", "Noah", "Olivia",
            "Penelope", "Quinn", "Ryan", "Sophia", "Theo"
        )
    }

    private fun generateRandomUsername(): List<String> {
        return listOf(
            "gamer123", "playmaster", "pixelwarrior", "consolequeen", "gamechaser",
            "joystickjunkie", "levelup", "gamerlife", "virtualhero", "gameon",
            "elitegamer", "powerplayer", "videogamepro", "strategygamer", "gamingexpert",
            "championplayer", "mastergamer", "ultimategamer", "gamerlegend", "epicplayer"
        )
    }

    private fun generateRandomPost(): List<String> {
        return listOf(
            "Cada dia cresce mais a jogação no mundo dos games.",
            "Novo jogo lançado hoje! Quem está animado para jogar?",
            "Explorando novos mundos virtuais.",
            "Melhores momentos no meu jogo favorito!",
            "E aí, galera! Quais são os jogos que vocês estão jogando atualmente?",
            "Desafio aceito: complete essa missão difícil!",
            "Aprendendo novas estratégias de jogo. Alguma dica?",
            "Compartilhando minha configuração de teclado gamer. #GamingSetup",
            "Game over ou continue? A escolha é sua!",
            "Deixe nos comentários qual é o seu jogo preferido!",
            "Jogando até altas horas da madrugada. Quem mais está acordado?",
            "Partida épica de multiplayer online. Vitória garantida!",
            "Atualizando meu setup com as últimas tecnologias.",
            "Novo DLC disponível! Corra para baixar e aproveitar.",
            "Capturando momentos épicos de jogo em vídeo. Em breve no canal!",
            "Conquistando novos níveis e desbloqueando conquistas.",
            "Estou transmitindo ao vivo agora! Venha assistir e interagir.",
            "Montando um time imbatível. Procuro companheiros de equipe!",
            "Preparando-me para o próximo torneio. Treino duro, jogo fácil.",
            "Ganhando destaque na comunidade gamer. Obrigado pelo apoio!",
            "Criando mods personalizados para meu jogo favorito."
        )
    }


}