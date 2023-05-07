package engtelecom.std;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.google.gson.Gson;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;
import org.zeromq.ZMQ.Socket;

import lombok.extern.java.Log;

@Log
public class Cliente<Dispositivo> {

    public List<String> processarResposta(ZMsg resposta) {
        List<String> quadros = new ArrayList<>();
        // Obtendo o primeiro frame da mensagem
        Optional<ZFrame> op = resposta.stream().findFirst();
        if (op.isPresent()) {
            log.info("Resposta do servidor: " + op.get().getString(ZMQ.CHARSET));
            // Obtendo os demais frames da mensagem
            resposta.stream().skip(1).forEach(frame -> {
                quadros.add(frame.getString(ZMQ.CHARSET));
            });
        }
        return quadros;
    }

   
    public void adicionar(){
       

        var local = new Local(1, "quarto", null);

        local.adicionarDispositivos("lampada", "desligado");

        var gson = new Gson();
        List<String> respostaDoServidor;
       
        try (ZContext context = new ZContext()) {
            log.info("Conectando no servidor");
            ZMQ.Socket socket = context.createSocket(SocketType.REQ);
            socket.connect("tcp://localhost:5555");
            System.err.println("--------------------------------");
            
            var mensagem = new ZMsg();
            var operacao = new ZFrame("adicionar");

            mensagem.add(operacao);
            mensagem.add(gson.toJson(local));
            mensagem.send(socket);

            respostaDoServidor = this.processarResposta(ZMsg.recvMsg(socket));
            respostaDoServidor.forEach(System.out::println);
            //System.err.println("====================================");
        }
    }
    

    public void buscar(){
        var gson = new Gson();
        
        List<String> respostaDoServidor;
        
        
        System.err.println("--------------------------------");
        System.err.println("Buscando pelo contato com ID: 1");

        try (ZContext context = new ZContext()) {
            //System.err.println("====================================");
            log.info("Conectando no servidor");
            ZMQ.Socket socket = context.createSocket(SocketType.REQ);
            socket.connect("tcp://localhost:5555");
            System.err.println("--------------------------------");
            
            var mensagem = new ZMsg();
            var operacao = new ZFrame("buscar");

            
            mensagem.add(operacao);
            mensagem.add(Integer.toString(1));
            mensagem.send(socket);
            respostaDoServidor = this.processarResposta(ZMsg.recvMsg(socket));
            respostaDoServidor.forEach(quadro -> {
                Local p = gson.fromJson(quadro, Local.class);
                System.err.println("Dispositivo:"+ p.getDispositivo()+ " "+"Estado: "+ p.getEstado());
            });
        }
    }

    public void ligar(){

        var gson = new Gson();
        List<String> respostaDoServidor;

        try (ZContext context = new ZContext()) {
            log.info("Conectando no servidor");
            ZMQ.Socket socket = context.createSocket(SocketType.REQ);
            socket.connect("tcp://localhost:5555");

            var mensagem = new ZMsg();
            var operacao = new ZFrame("ligar");
            System.err.println("--------------------------------");
            System.err.println("Ligar a lâmpada");
            
            mensagem = new ZMsg();
            operacao = new ZFrame("ligar");
            
            mensagem.add(operacao);
            mensagem.add(Integer.toString(1));
            mensagem.send(socket);
            respostaDoServidor = this.processarResposta(ZMsg.recvMsg(socket));
            respostaDoServidor.forEach(quadro -> {
                Local p = gson.fromJson(quadro, Local.class);
                System.err.println("Dispositivo:"+ p.getDispositivo());
                System.err.println("Estado: "+ p.getEstado());
            });
    }
}


    public void enviarMensagens() {
       
        adicionar();
        System.err.println("====================================");

        buscar();
        System.err.println("====================================");

        ligar();
        System.err.println("====================================");

    }

       
        /*
         var gson = new Gson();
        List<String> respostaDoServidor;
     Local local = adicionar();

        try (ZContext context = new ZContext()) {
            System.err.println("====================================");
            log.info("Conectando no servidor");
            ZMQ.Socket socket = context.createSocket(SocketType.REQ);
            socket.connect("tcp://localhost:5555");
            System.err.println("====================================");

            var mensagem = new ZMsg();
            var operacao = new ZFrame("adicionar");

            System.err.println("====================================");
            System.err.println("Status do Dispositivo: Ligar a lâmpada");
            
            mensagem = new ZMsg();
            operacao = new ZFrame("ligar");
            
            mensagem.add(operacao);
            mensagem.add(Integer.toString(1));
            mensagem.send(socket);
            respostaDoServidor = this.processarResposta(ZMsg.recvMsg(socket));
            respostaDoServidor.forEach(quadro -> {
                Local p = gson.fromJson(quadro, Local.class);
                System.err.println("Dispositivo retornado: " + p);
            });
            System.err.println("====================================");
 */
        
    
    
    public static void main(String[] args) {
    Cliente app = new Cliente();
    app.enviarMensagens();
    }
    

}
