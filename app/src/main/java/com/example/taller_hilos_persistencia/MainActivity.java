package com.example.taller_hilos_persistencia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity {


    Hilo1 hilo1;
    Button btnEnviarMensaje, btnInsertArea;
    Dialog dialog;
    LinearLayout linearLayout;
    EditText txtmensajee;
    boolean estado;
    String mensaje;
    int colors[] = {0xffd9534f, 0xffe41b23, 0xfff0ad4e, 0xff5cb85c, 0xFF018786};
    int i = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnEnviarMensaje = (Button) findViewById(R.id.btnEnviarMensaje);
        linearLayout = (LinearLayout)findViewById(R.id.linerLayout);

        dialog =  new Dialog( this );
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                estado = false;
            }
        });
        btnEnviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPanelMensaje();
            }
        });
        if( savedInstanceState != null){
            estado = savedInstanceState.getBoolean("estado");
            if(estado == true){
                showPanelMensaje();
                mensaje = savedInstanceState.getString("mensaje");
                txtmensajee.setText(mensaje);
            }
            colors = savedInstanceState.getIntArray("lista");
            i = savedInstanceState.getInt("contador");
        }



    }



    public class Hilo1 extends AsyncTask<Void, Integer, Void> { // parametros, progreso, resultado
        boolean state;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            state = true;
            //paused = false;
        }
        @Override
        protected Void doInBackground(Void... voids) { // NO TENEMOS ACCESO A LA UI

            while (state) {
                try {
                    publishProgress(0);
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;

        }
        public void parar(){
            state = false;
        }

        @Override
        protected void onProgressUpdate(Integer... enteros) {// POR MEDIO DE ESTE MÉTODO SE ACTUALIA LA UI
            super.onProgressUpdate(enteros);
            Toast.makeText(MainActivity.this, "llege", Toast.LENGTH_SHORT).show();

            if(i < colors.length){
                linearLayout.setBackgroundColor(colors[i]);
                i++;

            }else{
                i=0;
            }



        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("estado", estado);
        if(estado == true){
            outState.putString("mensaje",txtmensajee.getText().toString());
        }
        outState.putIntArray("lista",colors);
        outState.putInt("contador",i);
    }
    public void showPanelMensaje(){

        dialog.setContentView( R.layout.activity_mensajes );
        btnInsertArea = (Button)dialog.findViewById( R.id.btnInsertArea );
        txtmensajee = (EditText)dialog.findViewById( R.id.txtInsertAreaName );

        btnInsertArea.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ){
                Toast.makeText(MainActivity.this, "Mensaje Enviado", Toast.LENGTH_SHORT).show();
                //txtmensajee.setText(String.valueOf(mensaje));
                txtmensajee.setText("");
            }

        } );
        estado = true;
        dialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
        dialog.show();
        //dialog.onSaveInstanceState();
    }

    @Override
    public void onStart() {
        super.onStart();
        hilo1 = new Hilo1();
        hilo1.execute();
    }
    @Override
    public void onStop() {

        super.onStop();
        hilo1.parar();
        hilo1.cancel( true );
        Toast.makeText(MainActivity.this, "Cerro Hilo", Toast.LENGTH_SHORT).show();

    }




}