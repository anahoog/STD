package engtelecom.std;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import engtelecom.std.cloud.Local;
import engtelecom.std.cloud.Resposta;
import engtelecom.std.cloud.CloudGrpc.CloudImplBase;
import io.grpc.stub.StreamObserver;

public class CloudImpl extends CloudImplBase {

    // Serviço de log para registrar as mensagens de depuração, informação, erro,
    // etc.
    private static final Logger logger = Logger.getLogger(CloudImpl.class.getName());

    // Banco de dados para armazenar todos os contatos
    private Map<Integer, Local> cloud;

    public CloudImpl() {
        this.cloud = new HashMap<>();
    }

    @Override
    public void adicionar(Local request, StreamObserver<Resposta> responseObserver) {

        String mensagem = "id do contato já existe no banco de dados";

        if (!this.cloud.containsKey(request.getId())) {
            this.cloud.put(request.getId(), request);
            mensagem = "Contato com o id " + request.getId() + " foi adicionado com sucesso";
        }

        logger.info(mensagem);
        // Padrão de projeto Builder. Veja mais em
        // https://java-design-patterns.com/patterns/builder/
        Resposta resposta = Resposta.newBuilder().setResultado(mensagem).build();
        responseObserver.onNext(resposta);
        responseObserver.onCompleted();
    }

    @Override
    public void buscar(Local request, StreamObserver<Local> responseObserver) {
        Local resposta = this.cloud.get(request.getId());
        logger.info("Buscar... " + resposta);
        responseObserver.onNext(resposta);
        responseObserver.onCompleted();
    }
}
