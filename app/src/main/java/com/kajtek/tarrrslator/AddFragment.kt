package com.kajtek.tarrrslator

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.gson.JsonDeserializer
import okhttp3.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.set
import com.google.gson.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import okhttp3.*
import java.io.IOException
import java.lang.reflect.Type
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AddFragment: Fragment(), View.OnClickListener {

    private lateinit var englishEditText: EditText
    private lateinit var pirateEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val routeRecycler = inflater.inflate(R.layout.fragment_add, container, false)

        englishEditText = routeRecycler.findViewById(R.id.english_sentence)
        pirateEditText = routeRecycler.findViewById(R.id.pirate_tentence)

        val translate_button: Button = routeRecycler.findViewById<View>(R.id.translate_button) as Button
        translate_button.setOnClickListener(this)
        val add_button: Button = routeRecycler.findViewById<View>(R.id.add_button) as Button
        add_button.setOnClickListener(this)

        return routeRecycler
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.translate_button -> onClickTranslate()
                R.id.add_button -> onClickAdd()
            }
        }
    }

    private fun onClickAdd() {
        val englishText = englishEditText.text.toString()
        val pirateText = pirateEditText.text.toString()

        if (englishText.isEmpty() || pirateText.isEmpty()) {
            Toast.makeText(activity, resources.getString(R.string.empty_english_or_pirate_toast_message), Toast.LENGTH_LONG).show()
        }
        else {
            db.addTranslation(login, englishText, pirateText)
            Toast.makeText(activity, resources.getString(R.string.successful_addition_of_translation_toast_message), Toast.LENGTH_LONG).show()
            englishEditText.text.clear()
            pirateEditText.text.clear()

        }
    }

    private fun onClickTranslate() {
        val text = englishEditText.text.toString()
        val url = "https://api.funtranslations.com/translate/pirate.json?text=$text"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        var translatedText: String = ""

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    val fetched = response.body!!.string()

                    println(fetched)

                    val gson = GsonBuilder()
                        .registerTypeAdapter(Contents::class.java, MyDeserializer())
                        .create()

                    val c: Contents = gson.fromJson(fetched, Contents::class.java)
                    GlobalScope.launch {
                        withContext(Dispatchers.Main) {
                            pirateEditText.setText(c.translated.toString())
                        }
                    }

                }
            }
        })
        pirateEditText.setText(translatedText)
    }
}

data class Contents(val translated: String, val text: String, val translation: String)

internal class MyDeserializer : JsonDeserializer<Contents?> {
    @Throws(JsonParseException::class)
    override fun deserialize(
        je: JsonElement,
        type: Type?,
        jdc: JsonDeserializationContext?
    ): Contents {
        // Get the "content" element from the parsed JSON
        val content = je.asJsonObject["contents"]

        // Deserialize it. You use a new instance of Gson to avoid infinite recursion
        // to this deserializer
        return Gson().fromJson(content, Contents::class.java)
    }
}

interface ContentsCallback {
    fun onSuccessResponse(result: String?)
}

