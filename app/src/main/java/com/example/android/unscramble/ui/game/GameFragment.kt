package com.example.android.unscramble.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.unscramble.R
import com.example.android.unscramble.databinding.GameFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Fragment where the game is played, contains the game logic.
 */
class GameFragment : Fragment() {

    private val viewModel: GameViewModel by viewModels()


    // Menghungkan objek dengan layoout game_fragment.xml
    private lateinit var binding: GameFragmentBinding

    // Untuk  Meng-inflate XML tata letak game_fragment menggunakan objek binding.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Untuk mengembalikan instan objek yang mengikat
        binding = GameFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

        //  Untuk menyiapkan pemroses klik tombol dan mengupdate UI
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       // Untuk membaca aktivitas tombol submit dan skip
        binding.submit.setOnClickListener { onSubmitWord() }
        binding.skip.setOnClickListener { onSkipWord() }

        // Update score dan wordcount
        updateNextWordOnScreen()
        binding.score.text = getString(R.string.score, 0)
        binding.wordCount.text = getString(
            R.string.word_count, 0, MAX_NO_OF_WORDS)
    }

    //     Fungsi ini untuk mengecek kata yang diketik user, dan memperbarui skor jika jawaban benar. dan menampilkan kata acak

    private fun onSubmitWord() {
        val playerWord = binding.textInputEditText.text
        if (viewModel.isUserWordCorrect(playerWord.toString())) {
            setErrorTextField(false)
            if (viewModel.nextWord()) {
                updateNextWordOnScreen()
            } else {
                showFinalScoreDialog()
            }
        } else {
            setErrorTextField(true)
        }
    }

//    Untuk lanjut atau melawati kata yang tertera, namun tidak melakukan perubahan skor
    private fun onSkipWord() {
        if (viewModel.nextWord()) {
            setErrorTextField(false)
            updateNextWordOnScreen()
        } else {
            showFinalScoreDialog()
        }
    }


    //  Untuk menghapus konten kolom teks dan mereset status error
    private fun setErrorTextField(error: Boolean) {
        if (error) {
            binding.textField.isErrorEnabled = true
            binding.textField.error = getString(R.string.try_again)
        } else {
            binding.textField.isErrorEnabled = false
            binding.textInputEditText.text = null
        }
    }

    //    Untuk menampilkan kata baru dengan ejaan yang diacak
    private fun updateNextWordOnScreen() {
        binding.textViewUnscrambledWord.text = viewModel.currentScrambleWord
    }

//    Untuk menampilkan alert skor terakhir
    private fun showFinalScoreDialog() {
        MaterialAlertDialogBuilder(requireContext()).setTitle(getString(R.string.congratulations))
            .setMessage(getString(R.string.you_scored, viewModel.score))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.exit)) { _, _ ->
                exitGame()
            }
            .setPositiveButton(getString(R.string.play_again)) { _, _ ->
                restartGame()
            }
            .show()
    }

//  Memperbarui viewmodel dan tampilan UI
    private fun restartGame() {
        viewModel.reinitializeData()
        setErrorTextField(false)
        updateNextWordOnScreen()
    }

//  Keluar dari game
    private fun exitGame() {
        activity?.finish()
    }

}