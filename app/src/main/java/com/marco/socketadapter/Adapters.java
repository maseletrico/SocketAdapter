package com.marco.socketadapter;

import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.pow;
import static java.net.InetSocketAddress.createUnresolved;

public class Adapters extends AppCompatActivity {

    private ImageButton addButton;
    private TextView tvApplianceA,tvApplianceB;
    private EditText inputLabel;
    private EditText inputSerialNo;
    private ImageView imageViewTamper,imageViewOnOffA,imageViewOnOffB;
    private List<Model> socketList = new ArrayList<>();
    private SocketAdapter socketAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private Model model;
    private Integer tamperImageID,onOffAID,onOffBID;

    private ServerSocket serverSocket;
    public  final int SERVER_PORT = 3000;
    Thread serverThread = null;
    private Socket socket;
    private String read;
    private InetSocketAddress inetSocketAddress;
    private String dadosRecebidos[];
    //NsdHelper Class
    private NsdHelper nsdHelper;

    private TcpClient mTcpClient;

    private AsyncTask myAsynctask;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adapters);

        addButton = findViewById(R.id.imageButton_add_socket);
        inputLabel = findViewById(R.id.editText_socket_label);
        inputSerialNo = findViewById(R.id.editText_serial_number);
        imageViewTamper = findViewById(R.id.iv_tamper);
        //set tag green button as default
        imageViewTamper.setTag(R.mipmap.green_button);
        imageViewOnOffA = findViewById(R.id.iv_onOffA);
        imageViewOnOffB = findViewById(R.id.iv_onOffB);

        tvApplianceA = findViewById(R.id.tv_appliance_a);
        tvApplianceB = findViewById(R.id.tv_appliance_b);

        recyclerView = findViewById(R.id.rv_socket);
        socketAdapter = new SocketAdapter(socketList);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setAdapter(socketAdapter);
        //Bach Arrow
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        inputSerialNo.setFocusable(false);
        inputSerialNo.setFocusableInTouchMode(true);

        inputLabel.setFocusable(false);
        inputLabel.setFocusableInTouchMode(true);

        loadSocketAdaptersInfo();

        context = this;

//        nsdHelper = new NsdHelper(this);
//        nsdHelper.initializeNsd();
//        nsdHelper.discoverServices();

//        new Runnable(){
//            public void run() {

        networkSniffTask asyncTask = new networkSniffTask();
        asyncTask.execute();
//            }
//        };



//        this.serverThread = new Thread(new ServerThread());
//        this.serverThread.start();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fieldLabel = String.valueOf(inputLabel.getText());
                String fieldSerialNo = String.valueOf(inputSerialNo.getText());
                //test if edit text is empty
                if(fieldLabel.isEmpty() || fieldSerialNo.isEmpty()){
                    Toast.makeText(Adapters.this, "Field LABEL or field SERIAL No. can not be empty", Toast.LENGTH_SHORT).show();
                }else{
                    model = new Model();
                    model.setLabel_A(String.valueOf(inputLabel.getText()+"-A"));
                    model.setLabel_B(String.valueOf(inputLabel.getText()+"-B"));
                    model.setTemperature("25");
                    model.setSocketA_On(R.mipmap.white_button);
                    model.setTagA_On(R.mipmap.white_button);
                    model.setSocketB_On(R.mipmap.white_button);
                    model.setTagB_On(R.mipmap.white_button);
                    tamperImageID =getImageID(imageViewTamper);
                    switch(tamperImageID) {
                        case R.mipmap.green_button:
                            model.setTamperResitant(R.mipmap.green_button);
                            model.setTagTR_On(R.mipmap.green_button);
                            break;
                        case R.mipmap.red_button:
                            model.setTamperResitant(R.mipmap.red_button);
                            model.setTagTR_On(R.mipmap.red_button);
                            break;
                    }

                    model.setLockedTimeOut(R.mipmap.red_button);
                    socketList.add(model);
                    socketAdapter.notifyDataSetChanged();

                    inputLabel.setText("");
                    inputSerialNo.setText("");
                    Toast.makeText(Adapters.this, "Socket Adapter Add Successful!", Toast.LENGTH_SHORT).show();

                }

            }
        });

        imageViewTamper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = (ImageView) v;
                tamperImageID =getImageID(v);

                switch(tamperImageID) {
                    case R.mipmap.green_button:
                        imageView.setImageResource(R.mipmap.red_button);
                        imageView.setTag(R.mipmap.red_button);
                        SendMessage("Tamper Resistant ON");
                        break;
                    case R.mipmap.red_button:
                        imageView.setImageResource(R.mipmap.green_button);
                        imageView.setTag(R.mipmap.green_button);
                        SendMessage("Tamper Resistant OFF");;
                        break;
                }
            }
        });

        //eecute TCP Client
        new ConnectTask().execute("");
    }

    private class networkSniffTask extends AsyncTask<Void, Void, Void> {

        private static final String TAG = "nstask";

//        private WeakReference<Context> mContextRef;
//
//        public networkSniffTask(Runnable context) {
//            mContextRef = new WeakReference<Context>((Context) context);
//        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(TAG, "Let's sniff the network");

            try {
                //Context context = mContextRef.get();

                if (context != null) {

                    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                    WifiInfo connectionInfo = wm.getConnectionInfo();
                    int ipAddress = connectionInfo.getIpAddress();
                    String ipString = Formatter.formatIpAddress(ipAddress);


                    Log.d(TAG, "activeNetwork: " + String.valueOf(activeNetwork));
                    Log.d(TAG, "ipString: " + String.valueOf(ipString));

                    String prefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);
                    Log.d(TAG, "prefix: " + prefix);

                    for (int i = 0; i < 255; i++) {
                        String testIp = prefix + String.valueOf(i);

                        InetAddress address = InetAddress.getByName(testIp);
                        boolean reachable = address.isReachable(150);
                        String canonicalName = address.getCanonicalHostName();
                        String hostName = address.getHostName();
                        String hostAddress = address.getHostAddress();


                        if (reachable)
                            Log.d(TAG, "Host: " + hostName + " - " + canonicalName + " - " + hostAddress);

                    }

                }
            } catch (Throwable t) {
                Log.e(TAG, "Well that's not good.", t);
            }

            return null;
        }
    }

    public class ConnectTask extends AsyncTask<String, String, TcpClient> {

        @Override
        protected TcpClient doInBackground(String... message) {

            //we create a TCPClient object
            mTcpClient = new TcpClient(new TcpClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            });
            mTcpClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //response received from server
            Log.d("test", "response " + values[0]);
            //process server response here....

        }
    }

    private void SendMessage(String message){
        //sends the message to the server
        if (mTcpClient != null) {
            mTcpClient.sendMessage(message);
        }
    }

    private void loadSocketAdaptersInfo() {
        model = new Model();
        model.setLabel_A("Living Room1A");
        model.setLabel_B("Living Room1B");
        model.setTemperature("25");
        model.setSocketA_On(R.mipmap.green_button);
        model.setSocketB_On(R.mipmap.green_button);
        model.setTamperResitant(R.mipmap.green_button);
        model.setLockedTimeOut(R.mipmap.red_button);
        model.setApplianceA("Saw Power");
        model.setApplianceB("Table Lamp");
        socketList.add(model);

        model = new Model();
        model.setLabel_A("Dinig Room1A");
        model.setLabel_B("Dinig Room1B");
        model.setTemperature("25");
        model.setSocketA_On(R.mipmap.red_button);
        model.setSocketB_On(R.mipmap.green_button);
        model.setTamperResitant(R.mipmap.green_button);
        model.setLockedTimeOut(R.mipmap.red_button);
        model.setApplianceA("Generic");
        model.setApplianceB("TV");
        socketList.add(model);

        model = new Model();
        model.setLabel_A("Rest Room1A");
        model.setLabel_B("Rest Room1B");
        model.setTemperature("25");
        model.setSocketA_On(R.mipmap.green_button);
        model.setSocketB_On(R.mipmap.red_button);
        model.setTamperResitant(R.mipmap.green_button);
        model.setLockedTimeOut(R.mipmap.red_button);
        model.setApplianceA("Generic");
        model.setApplianceB("Hair dryer");
        socketList.add(model);

        model = new Model();
        model.setLabel_A("kitchenA");
        model.setLabel_B("Kitchen1B");
        model.setTemperature("25");
        model.setSocketA_On(R.mipmap.red_button);
        model.setSocketB_On(R.mipmap.red_button);
        model.setTamperResitant(R.mipmap.green_button);
        model.setLockedTimeOut(R.mipmap.red_button);
        model.setApplianceA("Generic");
        model.setApplianceB("Generic");
        socketList.add(model);

        model = new Model();
        model.setLabel_A("Bedroom1A");
        model.setLabel_B("Bedroom1B");
        model.setTemperature("25");
        model.setSocketA_On(R.mipmap.green_button);
        model.setSocketB_On(R.mipmap.green_button);
        model.setTamperResitant(R.mipmap.green_button);
        model.setLockedTimeOut(R.mipmap.red_button);
        model.setApplianceA("Generic");
        model.setApplianceB("Generic");
        socketList.add(model);

        model = new Model();
        model.setLabel_A("TV room1A");
        model.setLabel_B("TV room1B");
        model.setTemperature("25");
        model.setSocketA_On(R.mipmap.red_button);
        model.setSocketB_On(R.mipmap.green_button);
        model.setTamperResitant(R.mipmap.green_button);
        model.setLockedTimeOut(R.mipmap.red_button);
        model.setApplianceA("Generic");
        model.setApplianceB("Generic");
        socketList.add(model);

        socketAdapter.notifyDataSetChanged();
    }

    private Integer getImageID (View v){
        tamperImageID = (Integer) v.getTag();
        tamperImageID = tamperImageID == null ? 0 : tamperImageID;

        return tamperImageID;
    }

    private class ServerThread implements Runnable {

        //ServerThread class - run method
        @Override
        public void run() {
            try {
                if (!serverSocket.isBound() && !serverSocket.isClosed()) {
                    Log.i("SOCKET_LOG ", "Binding Socket");
                    serverSocket.setReuseAddress(true);
                    serverSocket.bind(new InetSocketAddress(SERVER_PORT)); // <-- now bind it
                    //serverSocket.bind(new InetSocketAddress(InetAddress.getLocalHost(),SERVER_PORT));
                    //serverSocket.bind(new InetSocketAddress("192.168.0.22",SERVER_PORT));
                    //serverSocket.bind(new InetSocketAddress("127.0.0.1",SERVER_PORT));

                    Log.i("SOCKET_LOG ", "Criado e Vinculado a porta " + SERVER_PORT);
                    //Log.i("SOCKET_LOG ", "Host Name " + inetSocketAddress.getHostName());

                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("SOCKET_LOG ", "Erro Criando e veiculando a porta "+ String.valueOf(e));
                if(!serverSocket.isClosed()){
                    try {
                        serverSocket.close();
                        Log.i("SOCKET_LOG ", "SOCKET CLOSED OK");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        Log.i("SOCKET_LOG ", "CLOSED ERR "+ String.valueOf(e1));
                    }

                }
            }

            if (!Thread.currentThread().isInterrupted()) {
                Log.i("SOCKET_LOG ", "Se Current Thread.isInterrupted.. ");
                if (!serverSocket.isClosed()) {
                    Log.i("SOCKET_LOG ", "Se socket não está fechado");
                    try {
                        socket = serverSocket.accept();
                        Log.i("SOCKET_LOG ", "Socket Accepted");
                        CommunicationThread commThread = new CommunicationThread(socket);
                        new Thread(commThread).start();
                        Log.i("SOCKET_LOG ", "commThread Started");
                        //CountThread++;
                        Log.i("SOCKET_LOG ", "GET LOCAL PORT " + serverSocket.getLocalPort());
                        Log.i("SOCKET_LOG ", "GET INET ADDRESS " + serverSocket.getInetAddress());
                        Log.i("SOCKET_LOG ", "GET CHANNEL " + serverSocket.getChannel());
                        Log.i("SOCKET_LOG ", "GET LOCAL SOCKET ADDRESS " + serverSocket.getLocalSocketAddress());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.i("SOCKET_LOG ", "Erro new Thread(commThread).start() " + e.toString());

                    }
                }
            }else{
                Log.i("SOCKET_LOG ", "Current Thread.isInterrupted = true ");
            }
        }
    }

    class CommunicationThread implements Runnable {

        private Socket socket;
        private BufferedReader input;
        //Socket Writer
        private BufferedWriter output;

        CommunicationThread(Socket mSocket) {
            this.socket = mSocket;

            try {
                //Socket Reader
                this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                //Socket Writer
                this.output = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
                //no time out
                this.socket.setSoTimeout(0);

            } catch (SocketTimeoutException e) {
                if (socket.isConnected()) {
                    Log.d("SOCKET_LOG ", "Timout(conectado) ao criar buffer Ler/Escrever/" + e.toString());
                }
                Log.d("SOCKET_LOG ", "Timout(desconectado) ao criar buffer Ler/Escrever/" + e.toString());
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("SOCKET_LOG ", "Exxception ao criar buffer Ler/Escrever/" + e.toString());
            }
        }

        //CommunicationThread Class - run method
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
//                if(RUN_SOCKET_STOP){
//                    Thread.currentThread().interrupt();
//                }
                try {
                    read = input.readLine();
                    //updateConversationHandler.post(new updateUIThread(read));
                    if(read!=null){
                        if(!read.isEmpty()) {
                            Log.d("SOCKET_LOG ","read length "+  String.valueOf(read.length()));
                            updateUIThread(read);
                        }
                    }else{
                        Log.d("SOCKET_LOG ","READ NULL");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("SOCKET_LOG ","Erro ao tentar ler readLine() "+e.toString());
                    //SERVER_SOCKET4 :: java.net.SocketException: recvfrom failed: ETIMEDOUT (Connection timed out)
                }
                //Socket Writer
                //if(!eventoTxHardware) {

                try {
                    output.write("hello from Server");
                    //output.newLine();
                    //output.write(userName);
                    output.newLine();
                    output.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    void updateUIThread(String str) {
        String msg = str;

        if(msg!=null) {
            //Separa String recebida
            dadosRecebidos = msg.split("\\|");

//            if(dadosRecebidos.length ==9 || dadosRecebidos.length ==10){
//
                Log.i("SOCKET_LOG String ", String.valueOf(dadosRecebidos.length) + "-" + msg.toString());
////                numDePulsosEm200ms = dadosRecebidos[0];
//
//            }else {
//
////                Log.i("SOCKET_LOG String incompleta ", String.valueOf(dadosRecebidos.length)+"-"+String.valueOf(msg)+ " Length "+ dadosRecebidos.length);
////                SOCKET_TIMEOUT = false;
//            }

        }else{
            //Log.i("SOCKET_LOG String Null ", String.valueOf(dadosRecebidos.length)+"-"+String.valueOf(msg)+ " Length "+ dadosRecebidos.length);

        }
    }

    //Método de alerta para criação do socket
    private void alertDialogSocket(String erro){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("SOCKET!!")
                .setCancelable(false)
                .setMessage("Socket Create Error! "+ erro)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //qrCodeReader(horimetro,hodometro,-1);

                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

        //for negative side button
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));
        //for positive side button
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
    }
    @Override
    protected void onResume() {
        super.onResume();

    }
    @Override
    protected void onPause(){
        if (mTcpClient != null) {
            mTcpClient.stopClient();
        }
        super.onPause();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
