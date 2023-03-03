package appnet.demo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.*;

public class MainActivity extends AppCompatActivity {

    IWoyouService woyouService;
    ICallback callback = new ICallback.Stub() {

        @Override
        public void onRunResult(boolean success) throws RemoteException {
        }

        @Override
        public void onReturnString(final String value) throws RemoteException {
        }

        @Override
        public void onRaiseException(int code, final String msg)
                throws RemoteException {
        }

        @Override
        public void onPrintResult(int code, String msg) throws RemoteException {

        }
    };
    ServiceConnection connService = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            woyouService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            woyouService = IWoyouService.Stub.asInterface(service);
        }
    };

    Button imp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imp=(Button)findViewById(R.id.btnimprimir);

        Intent i =getIntent();
        Intent intent = new Intent();
        intent.setPackage("woyou.aidlservice.jiuiv5");
        intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        startService(intent);//Comience el servicio de impresión
        bindService(intent, connService, Context.BIND_AUTO_CREATE);


        imp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        ImprimirLogo();
                        ImprimirDet();
                    }
                };

                thread.start();


            }
        });

    }

    private void ImprimirLogo() {

        try {

            Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.drawable.logo2);
            icon = Bitmap.createScaledBitmap(icon,350,120,false);
            woyouService.printBitmap(icon,callback);



        } catch (RemoteException e) {
            Toast.makeText(getApplicationContext(), "NO es Posible Imprimir " ,	Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void ImprimirDet() {

        try {



            woyouService.printText( "\n", callback);
            woyouService.setFontSize(25,callback);
            int valorunitario,total,iva,cantidad,neto;
            valorunitario=1500;
            cantidad=2;
            neto=cantidad*valorunitario;
            iva=(neto/100)*19;
            total=iva+neto;
            woyouService.printText( " Rut Empresa : 96.923.510-k\n", callback);
            woyouService.printText("Fecha Emision : 11-11-2018\n",callback);
            woyouService.printText("Giro : Distribucion gas licuado\n",callback);
            woyouService.printText("Cama Matriz : Apoquindo 5400, Piso 15, Las Condes, Santiago\n\n",callback);
            woyouService.printText("Gas licuado "+cantidad+" "+valorunitario+"\n\n",callback);
            woyouService.setFontSize(30,callback);
            woyouService.printText("neto : "+neto+"\n",callback);
            woyouService.printText("iva : "+iva+"\n",callback);
            woyouService.printText("total : "+total+"\n",callback);
            woyouService.printText("\n\n",callback);
            Bitmap icon2 = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.drawable.timbre);
            icon2 = Bitmap.createScaledBitmap(icon2,350,120,false);
            woyouService.printBitmap(icon2,callback);
            woyouService.printText("\n\n",callback);
            woyouService.printQRCode("https://www.lipigas.cl/",10,1,callback);
            woyouService.printText("\n\n",callback);
            woyouService.printText("\n\n",callback);
            woyouService.printText("\n\n",callback);

        } catch (RemoteException e) {
            Toast.makeText(getApplicationContext(), "NO es Posible Imprimir " ,	Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}
