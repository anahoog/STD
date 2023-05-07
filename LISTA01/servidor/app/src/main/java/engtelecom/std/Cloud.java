package engtelecom.std;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.gson.Gson;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import lombok.extern.java.Log;

@Log
public class Cloud {
    public static void main(String[] args) {
        // Para representar o banco de dados - a lista de locais
        Map<Integer, Local> lista = new HashMap<>();

        // Para ajudar na conversão de objetos Java em JSON
        Gson gson = new Gson();

        try (ZContext context = new ZContext()) {
            ZMQ.Socket socket = context.createSocket(SocketType.REP);
            socket.bind("tcp://*:5555");
            System.err.println(" ");
            log.info("Servidor pronto! Esperando por conexões");
            System.err.println(" ");


            while (!Thread.currentThread().isInterrupted()) {
                // Fica bloqueado até chegar uma mensagem
                ZMsg mensagem = ZMsg.recvMsg(socket);
                ZMsg msgResposta = new ZMsg();
                System.err.println("====================================");
                System.err.println("|| Chegou novo pedido do cliente  ||");
                System.err.println("====================================");
                System.err.println(" ");



                // Obtendo primeira mensagem (Zmsg)
                Optional<ZFrame> op = mensagem.stream().findFirst();

                if (op.isPresent()) {
                    ZFrame primeiroFrame = op.get();

                    // Verificando qual operação foi invocada pelo cliente
                    switch (primeiroFrame.getString(ZMQ.CHARSET)) {

                        case "adicionar":
                            //msgResposta.add("Dispositivo Adicionado com Sucesso");
                            mensagem.stream().skip(1).forEach(frame -> {
                                String json = frame.getString(ZMQ.CHARSET);
                                System.err.println("TIPO: Adicionar");
                                System.err.println(json);
                                System.err.println(" ");
                                Local p = gson.fromJson(json, Local.class);
                                
                                String resposta = "Existe um local com o ID (" + p.getId() + ") no servidor.";

                                if (lista.get(p.getId()) == null) {
                                    lista.put(p.getId(), p);
                                    System.err.println(" ");
                                    resposta = "ID: " + p.getId() + " : Dispositivo Adicionado";
                                  //  System.err.println("====================================");

                                }
                                System.err.println(resposta);
                                // adicionando um frame por Local processada
                                msgResposta.add(resposta);
                            });
                            break;

                        case "buscar":
                            System.err.println("TIPO: Buscar");

                            Optional<ZFrame> frameIdLocal = mensagem.stream().skip(1).findFirst();

                            if (frameIdLocal.isPresent()) {
                                int idLocal = Integer.parseInt(frameIdLocal.get().getString(ZMQ.CHARSET));

                                var resultado = lista.get(idLocal);

                                if (resultado != null) {
                                    msgResposta.add("Dispositivo Encontrado");
                                    msgResposta.add(gson.toJson(resultado));
                                    System.err.println("ID: " + idLocal);
                                    System.err.println(resultado.getNome());
                                    System.err.println(resultado.getEstado());


                                } else {
                                    msgResposta.add("não encontrado");
                                }
                            }
                            break;

                        case "ligar":
                            System.err.println("TIPO: Ligar");

                            Optional<ZFrame> ligar = mensagem.stream().skip(1).findFirst();

                            if (ligar.isPresent()) {
                                int idLocal = Integer.parseInt(ligar.get().getString(ZMQ.CHARSET));
                                Local resultado = lista.get(idLocal);

                                resultado.ligarDispositivo();
                                msgResposta.add("Dispositivo encontrado");
                                msgResposta.add(gson.toJson(resultado));
                                System.err.println("ID: " + idLocal);
                                System.err.println(resultado.getNome());
                                System.err.println(resultado.getEstado());

                                
                            }
                            break;
                        default:
                            System.err.println("cliente invocou uma operação desconhecida");
                            msgResposta.add("Operação desconhecida");
                    }
                    // Enviando a resposta
                    msgResposta.send(socket);
                    }
            }
        }
    }
}
