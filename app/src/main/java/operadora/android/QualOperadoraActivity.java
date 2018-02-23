package operadora.android;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class QualOperadoraActivity extends Activity {
	
    private static final int TIMEOUT_CONEXAO = 20000; // 20 segundos
    private static final int TIMEOUT_SOCKET = 30000; // 30 segundos
    private static final int TAM_MAX_BUFFER = 10240; // 10Kbytes
	
	private String url;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);   
        
       
        
        final TextView resposta = (TextView )findViewById(R.id.resp);
        

        
        Button buton = (Button)findViewById(R.id.button1);
        

        	buton.setOnClickListener(new Button.OnClickListener() {        		
        		public void onClick(View v) {
        			if(isOnline())
        				resposta.setText(getOperadora());        			
        			else
        				resposta.setText("Voc? n?o est? Conectado!");
        		}
        	});     
        
        
    }
    
    private boolean isOnline() {
    	 ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    	 return cm.getActiveNetworkInfo().isConnectedOrConnecting();

    	}
    

        
        public String getEstados(){
            String parserbuilder = "";
            
            String num_telefone = ((EditText)findViewById(R.id.num_telefone)).getText().toString();
            
            url = "http://consultanumero.telein.com.br/sistema/consulta_numero.php?numero="+num_telefone+"&chave=senhasite";
            
            
            try{
                HttpParams httpParameters = new BasicHttpParams();
                
                // Configura o timeout da conex?o em milisegundos at? que a conex?o
                // seja estabelecida
                HttpConnectionParams.setConnectionTimeout(httpParameters, TIMEOUT_CONEXAO);
                
                // Configura o timeout do socket em milisegundos do tempo 
                // que ser? utilizado para aguardar os dados
                HttpConnectionParams.setSoTimeout(httpParameters,TIMEOUT_SOCKET);   
                
                HttpClient httpclient = new DefaultHttpClient(httpParameters);
                HttpPost httppost = new HttpPost(url);
        
                HttpResponse response = httpclient.execute(httppost);
                
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent(),"UTF-8"), TAM_MAX_BUFFER);
                
                StringBuilder builder = new StringBuilder();
                
                //for (String line = null ; (line = reader.readLine())!= null;) {
                    builder.append(reader.readLine()); //.append("\n");
                //}
                
                parserbuilder = builder.toString();
                
                // Retira a string <?xml version="1.0" encoding="utf-8" ?> 
                // <string xmlns="http://tempuri.org/"> e a tag </string> 
                // para obter o resultado em Json, j? que o webservice est?
                // retornando uma string
                Integer firstTagString = parserbuilder.indexOf ("#");
               // Integer posXml = parserbuilder.indexOf(">", firstTagString);
               // Integer posTagString = parserbuilder.indexOf("</string>");
                parserbuilder = parserbuilder.substring(0, firstTagString);
               
            
            }catch(ClientProtocolException e){
                Log.e("WebService", e.toString());
            }
            catch(IOException e){
                Log.e("WebService", e.toString());
            }
            
            return parserbuilder;    
        }
        
        public String getOperadora(){
        	
        	String operadora = "";
        	
        	switch ( Integer.parseInt(this.getEstados())) {
			case 77:
				operadora = "Nextel";				
				break;
				
			case 23:
				operadora = "Telemig";				
				break;
				
			case 12:
				operadora = "CTBC";				
				break;
				
			case 14:
				operadora = "Brasil Telecom";				
				break;
				
			case 20:
				operadora = "VIVO";				
				break;
				
			case 21:
				operadora = "Claro";				
				break;
				
			case 31:
				operadora = "OI";				
				break;
				
			case 24:
				operadora = "AMAZONIA";				
				break;
				
			case 37:
				operadora = "UNICEL";				
				break;
				
			case 41:
				
				operadora = "TIM";				
				break;
				
			case 43:
				operadora = "SERCOMERCIO";				
				break;
				
			case 99:
				operadora = "N?MERO N?O ENCONTRADO";				
				break;
				
			case 999:
				operadora = "CHAVE INV?LIDA";				
				break;
				
			case 990:
				operadora = "IP BLACKED LISTED";				
				break;
				

			default:
				operadora = "Erro";
				break;
			}        	
        	
        	
        	return operadora;
        }
    
}