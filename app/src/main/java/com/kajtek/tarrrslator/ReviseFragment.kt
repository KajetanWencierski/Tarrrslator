package com.kajtek.tarrrslator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment


class ReviseFragment : Fragment(), View.OnClickListener {

    private lateinit var englishTextView: TextView
    private lateinit var pirateEditText: EditText
    private lateinit var translateTextView: TextView
    private lateinit var translations: ArrayList<Translation>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val routeRecycler =
            inflater.inflate(R.layout.fragment_revise, container, false)

        englishTextView = routeRecycler.findViewById(R.id.english_sentence)
        pirateEditText = routeRecycler.findViewById(R.id.pirate_tentence)
        translateTextView = routeRecycler.findViewById(R.id.translate_string)

        fetchTranslations()
        showNewTranslation()

        val check_button: Button = routeRecycler.findViewById<View>(R.id.check_button) as Button
        check_button.setOnClickListener(this)
        val refresh_button: Button = routeRecycler.findViewById<View>(R.id.refresh_button) as Button
        refresh_button.setOnClickListener(this)
        val delete_button: Button = routeRecycler.findViewById<View>(R.id.delete_button) as Button
        delete_button.setOnClickListener(this)

        return routeRecycler
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.check_button -> onClickCheck()
                R.id.refresh_button -> onClickRefresh()
                R.id.delete_button -> onClickDelete()
            }
        }
    }

    private fun onClickDelete() {
        if (translations.size > 0) {
            db.deleteTranslation(translations[0].id)

            fetchTranslations()
            showNewTranslation()
        }
    }

    private fun onClickRefresh() {
        fetchTranslations()
        showNewTranslation()
    }

    private fun onClickCheck() {
        if (translations.size == 0) {
            Toast.makeText(activity, R.string.no_translation_found_message, Toast.LENGTH_LONG).show()
        }
        else {
            val pirateText = pirateEditText.text.toString()
            val englishText = englishTextView.text.toString()

            val translation = translations.find { it.english == englishText}
            if (translation?.pirate == pirateText) {
                Toast.makeText(activity, R.string.correct_answer_message, Toast.LENGTH_LONG).show()
                fetchTranslations()
                showNewTranslation()
            }
            else {
                val message = getString(R.string.wrong_answer_message) + " " + translation?.pirate
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun fetchTranslations(){
        translations = db.getTranslations(login)
        translations.shuffle()
    }

    private fun showNewTranslation(){
        if (translations.size > 0) {
            translateTextView.text = getString(R.string.translate_string)
            englishTextView.text = translations[0].english
            pirateEditText.setText("")
        }
        else {
            translateTextView.text = getString(R.string.no_translation_found_message)
            englishTextView.text = ""
            pirateEditText.setText("")
        }
    }
}