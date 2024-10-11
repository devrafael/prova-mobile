package com.prova;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private final int NUM_LINHAS = 4;
    private final int NUM_COLUNAS = 8;
    private final String COR_SELECT = "#FFFBC02D";
    private final String COR_UNSELECT = "#44000000";
    private final String COR_FUNDO_LETRA = "#FFFFFF";
    private final boolean[] arrayQuads = new boolean[NUM_COLUNAS * NUM_LINHAS];
    Set<Character> letrasClicadas = new HashSet<>();

    private LayoutInflater inflater;
    private TableLayout tableLayout;
    private final HashMap<Integer, Button> btnMap = new HashMap<>();
    Character[] caracteresArray;
    private TextView tvMensagem;

    String[] teclado = {"R", "A", "F", "E", "L", "D", "O", "S", "N", "T", "M", "I",
            "2", "0", "8", "3", "1", " "};
    private int letrasReveladas = 0;
    StringBuilder nomeOculto =  new StringBuilder("******");
    StringBuilder sobrenomeOculto = new StringBuilder("*********************");
    StringBuilder matriculaOculta = new StringBuilder("*********");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inflater = LayoutInflater.from(this);
        tableLayout = findViewById(R.id.tl_letras_algarismos);
        int btnId = 0;

        String combinacao = "RAFAEL DOS SANTOS DE ALMEIDA 200028321";
        Set<Character> caracteresUnicos = new HashSet<>();
        for (char c : combinacao.toCharArray()) {
            caracteresUnicos.add(c);
        }
        caracteresArray = caracteresUnicos.toArray(new Character[0]);

        tvMensagem = findViewById(R.id.tv_mensagem);

        for (int i = 0; i < NUM_LINHAS; i++) {
            TableRow tableRow = new TableRow(this);

            for (int j = 0; j < NUM_COLUNAS; j++) {
                Button btn = (Button) inflater.inflate(R.layout.btn_template, tableRow, false);
                btn.setId(btnId);
                btn.setBackgroundColor(Color.parseColor(COR_UNSELECT));
                if (btnId < teclado.length) {
                    if (teclado[btnId].equals(" ")) {
                        btn.setText(" ");
                        btn.setBackgroundColor(Color.parseColor(COR_FUNDO_LETRA));
                    } else {
                        btn.setText(String.valueOf(teclado[btnId]));
                        btn.setBackgroundColor(Color.parseColor(COR_FUNDO_LETRA));
                    }
                } else {
                    btn.setText("");
                }
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Clicar(view);
                        revealLetter(btn, nomeOculto, "RAFAEL", R.id.tv_nome);
                        revealLetter(btn, sobrenomeOculto, "DOS SANTOS DE ALMEIDA", R.id.tv_sobrenome);
                        revealLetter(btn, matriculaOculta, "200028321", R.id.tv_matricula);
                        String letra = btn.getText().toString();
                        if (letra != null) {
                            letrasClicadas.add(letra.charAt(0));
                        }
                        verificarConclusao();
                    }
                });

                tableRow.addView(btn);
                btnMap.put(btnId, btn);
                btnId++;
            }
            tableLayout.addView(tableRow);
        }

        TextView tv_nome = findViewById(R.id.tv_nome);
        tv_nome.setText(nomeOculto);

        TextView tv_sobrenome = findViewById(R.id.tv_sobrenome);
        tv_sobrenome.setText(sobrenomeOculto);

        TextView tv_matricula = findViewById(R.id.tv_matricula);
        tv_matricula.setText(matriculaOculta);

    }

    public void handleEmbaralhar(View view) {
        this.embaralharTeclas();
    }

    public void handleReset(View view) {
        this.resetarEstado();
    }

    private void embaralharTeclas() {
        List<Character> listaCaracteres = Arrays.asList(caracteresArray);
        System.out.println(listaCaracteres);
        List<Character> listaCompleta = new ArrayList<>(listaCaracteres);
        System.out.println(listaCompleta);

        // Adiciona espaços até preencher o tamanho necessário
        while (listaCompleta.size() < NUM_LINHAS * NUM_COLUNAS) {
            listaCompleta.add(' ');
        }

        Collections.shuffle(listaCompleta);
        boolean primeiroEspacoEncontrado = false;
        System.out.println("letra clicada: " + letrasClicadas);
        for (int i = 0; i < btnMap.size(); i++) {
            Button btn = btnMap.get(i);
            if (btn != null && i < listaCompleta.size()) {
                Character letraAtual = listaCompleta.get(i);
                btn.setText(letraAtual == ' ' ? " " : String.valueOf(letraAtual));

                if (letraAtual == ' ') {

                    if (!primeiroEspacoEncontrado) {
                        btn.setBackgroundColor(Color.parseColor(COR_FUNDO_LETRA));
                        if (letrasClicadas.contains(letraAtual)) {
                            btn.setBackgroundColor(Color.parseColor(COR_SELECT));
                        }
                        primeiroEspacoEncontrado = true;
                    } else {
                        btn.setBackgroundColor(Color.parseColor(COR_UNSELECT));
                    }
                } else {
                    btn.setBackgroundColor(Color.parseColor(COR_FUNDO_LETRA));

                    if (letrasClicadas.contains(letraAtual)) {
                        btn.setBackgroundColor(Color.parseColor(COR_SELECT));
                    }
                }
            }
        }
    }

    private void resetarEstado() {
        tvMensagem.setVisibility(View.GONE);
        tvMensagem.setText("");

        nomeOculto = new StringBuilder("******");
        sobrenomeOculto = new StringBuilder("*********************");
        matriculaOculta = new StringBuilder("*********");
        letrasReveladas = 0;

        TextView tv_nome = findViewById(R.id.tv_nome);
        tv_nome.setText(nomeOculto.toString());

        TextView tv_sobrenome = findViewById(R.id.tv_sobrenome);
        tv_sobrenome.setText(sobrenomeOculto.toString());

        TextView tv_matricula = findViewById(R.id.tv_matricula);
        tv_matricula.setText(matriculaOculta.toString());

        for (int i = 0; i < btnMap.size(); i++) {
            Button btn = btnMap.get(i);
            if (btn != null) {
                btn.setEnabled(true);
                btn.setBackgroundColor(Color.parseColor(COR_UNSELECT));


                if (i < teclado.length) {
                    btn.setText(teclado[i]);
                    btn.setBackgroundColor(Color.parseColor(COR_FUNDO_LETRA));
                } else {

                    btn.setText("");
                }
                arrayQuads[i] = false;
            }

        }
        letrasClicadas.clear();
    }



    private void revealLetter(Button btn, StringBuilder oculto, String palavra, int textViewId) {
        String letra = btn.getText().toString();

        if (letra.equals("")) {
            letra = " ";
        }

        for (int i = 0; i < palavra.length(); i++) {
            if (palavra.charAt(i) == letra.charAt(0)) {

                if (oculto.charAt(i) == '*') {
                    oculto.setCharAt(i, letra.charAt(0));
                    letrasReveladas++;
                }
            }
        }
        TextView campo = findViewById(textViewId);
        campo.setText(oculto.toString());
    }

    private void verificarConclusao() {
        System.out.println("letrasReveladas: " + letrasReveladas);
        System.out.println("soma length: " + (nomeOculto.length() + sobrenomeOculto.length() + matriculaOculta.length()));
        if (letrasReveladas == (nomeOculto.length() + sobrenomeOculto.length() + matriculaOculta.length())) {

            tvMensagem.setText("PARABÉNS! VOCÊ COMPLETOU!");
            tvMensagem.setVisibility(View.VISIBLE);
        }
    }

    private void Clicar(View view) {
        int id = view.getId();
        if (!arrayQuads[id]) {
            view.setBackgroundColor(Color.parseColor(COR_SELECT));
            arrayQuads[id] = !arrayQuads[id];
            
        }
    }
}
