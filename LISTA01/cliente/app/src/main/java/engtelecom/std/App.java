package engtelecom.std;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import engtelecom.std.cloud.CloudGrpc;
import engtelecom.std.cloud.Local;
import engtelecom.std.cloud.Local.TipoDispositivo;
import engtelecom.std.cloud.Local.nomeDispositivo;
import io.grpc.ManagedChannelBuilder;

public class App {

    private static final Logger logger = Logger.getLogger(App.class.getName());

    public static void main(String[] args) throws Exception {

        // Por padrão o gRPC sempre será sobre TLS, como não criamos um certificado digital, forçamos aqui não usar TLS
        var channel = ManagedChannelBuilder.forTarget("localhost:50051").usePlaintext().build();

        // Criando um comodo usando o padrão de projeto Builder
        var casa = Local.newBuilder().setComodo("Suite Master")
            .setId(1)
            .addDispositivos(
                nomeDispositivo.newBuilder().setDispositivo("Suíte Master").setTipo(TipoDispositivo.lampada).build()).build();
            
        
        logger.info("Adicionando um comodo na lista da casa no servidor");

        var cloudBlockingStub = CloudGrpc.newBlockingStub(channel);

        cloudBlockingStub.adicionar(casa);

        logger.info("Comodo adicionado");

        logger.info("Buscando por um comodo na lista");

        var resultado = cloudBlockingStub.buscar(casa);

        logger.info("Dados do comodo retornada pelo servidor: " + resultado);
        
    
    logger.info("Finalizando...");
    
    channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
    }
}

