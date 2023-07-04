package com.example.pc4progmovil;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc4progmovil.service.JokeApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
// Nombre: Beatriz Ambrosio Santiago

public class MainActivity extends AppCompatActivity {
    private RadioGroup radioGroupCategoria, radioGroupIdioma;
    private Button obtenerChisteButton;
    private TextView resultadoTextView;
    String categoria, idioma;

    private JokeApiService jokeApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radioGroupCategoria = findViewById(R.id.rgCategoria);
        radioGroupIdioma = findViewById(R.id.rgIdioma);
        obtenerChisteButton = findViewById(R.id.btnObtenerChiste);
        resultadoTextView = findViewById(R.id.txtResultadoJSON);

        // Configura Retrofit
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://v2.jokeapi.dev/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(new OkHttpClient())
                .build();
        jokeApiService = retrofit.create(JokeApiService.class);

        obtenerChisteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = getCategory();
                String language = getLanguage();
                int selectedCategory = radioGroupCategoria.getCheckedRadioButtonId();
                int selectedLanguage = radioGroupCategoria.getCheckedRadioButtonId();

                if (selectedCategory == -1 || selectedLanguage == -1) {
                    Toast.makeText(MainActivity.this, "Por favor, seleccione una categoría y selecciona un idioma.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Realiza la solicitud a la API utilizando Retrofit
                Call<JsonObject> call = jokeApiService.getJokeCall(category, language);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()){
                            JsonObject jsonObject = response.body();

                            if (jsonObject !=null){
                                // Convierte el objeto JSON a una cadena de texto
                                //String jsonResponse = new Gson().toJson(jsonObject);
                                //resultadoTextView.setText(jsonResponse);

                                // Convierte el objeto JSON a una cadena de texto con formato
                                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                                JsonParser jsonParser = new JsonParser();
                                String formattedJson = gson.toJson(jsonParser.parse(jsonObject.toString()));
                                resultadoTextView.setText(formattedJson);

                            }
                        }else {
                            Toast.makeText(MainActivity.this, "Error en la respuesta de la API", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                        Toast.makeText(MainActivity.this, "Error al realizar la solicitud API", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private String getCategory(){

        if (radioGroupCategoria.getCheckedRadioButtonId()==R.id.rbCualquier){
            return categoria = "Any";
        }else if (radioGroupCategoria.getCheckedRadioButtonId()== R.id.rbProgramacion){
            return categoria = "Programming";
        }
        return categoria;
    }
    private String getLanguage(){
        if (radioGroupIdioma.getCheckedRadioButtonId()==R.id.rbIngles){
            return idioma = "en";
        }else if (radioGroupIdioma.getCheckedRadioButtonId()== R.id.rbEspañol){
            return idioma = "es";
        }
        return idioma;
    }
}



