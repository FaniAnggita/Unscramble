package com.example.android.unscramble.ui.game

import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

//     Variabel ini disiapkan untuk dipindahkan variabel data score, currentWordCount, currentScrambledWord ke class GameViewModel
    private var _score = 0
    val score: Int get() = _score

    private var _currentWordCount = 0
    val currentWordCount: Int get() = _currentWordCount

//    deklarasi currentScrambledWord untuk menambahkan properti pendukung. Sekarang _currentScrambledWord hanya dapat diakses dan diedit dalam GameViewModel
    private lateinit var _currentScrambledWord: String
    val currentScrambleWord: String get() = _currentScrambledWord


    private var usedWordsList: MutableList<String> = mutableListOf()
    private lateinit var currentWord: String

    private var _count = 0
    val count: Int get() = _count

    init {
        getNextWord()
    }

// Mengonversi  string currentWord ke array karakter dan tetapkan ke val baru yang disebut tempWord. Untuk mengacak kata, acak karakter dalam array ini menggunakan metode Kotlin, shuffle().
    private fun getNextWord() {
        currentWord = allWordsList.random()
        val scrambledWord = currentWord.toCharArray()
        scrambledWord.shuffle()
        if (usedWordsList.contains(currentWord)) {
            getNextWord()
        } else {
            while (scrambledWord.toString().equals(currentWord, false)) {
                scrambledWord.shuffle()
            }
            _currentScrambledWord = String(scrambledWord)
            ++_currentWordCount
            usedWordsList.add(currentWord)
        }
    }

//     Untuk menambahkan score
    private fun increaseScore(){
        _score += SCORE_INCREASE
    }

    fun isUserWordCorrect(playerWord : String) : Boolean{
        if(playerWord.equals(currentWord)){
            increaseScore()
            return true
        }else{
            return false
        }
    }

//    Mengembalikan nilai true jika jumlah kata saat ini kurang dari MAX_NO_OF_WORDS.
    fun nextWord(): Boolean {
        return if (_currentWordCount < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else {
            false
        }
    }

//  Inisialisasi ulang data game untuk memulai ulang game
    fun reinitializeData(){
        _score = 0
        _currentWordCount = 0
        usedWordsList.clear()
        getNextWord()
    }
}