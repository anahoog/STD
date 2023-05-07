package engtelecom.std;
import engtelecom.std.Dispositivos;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import lombok.NonNull;

@Data
public class Local { //local = casa
    @NonNull
    private Integer id;

    @NonNull
    private String nome; //nome=comodo

    private Dispositivos listaDispositivos;


    public String adicionarDispositivos(String string, String b){
        this.listaDispositivos = new Dispositivos(string, b);
        return string;
        
        
    }
    public void ligarDispositivo(){
        this.listaDispositivos.ligar();        
        
    }
    public String getEstado(){
        return this.listaDispositivos.getEstado();
    }
    public String getDispositivo(){
        return listaDispositivos.getTipo();
    }
    public Local(@NonNull Integer id, @NonNull String nome, Dispositivos listaDispositivos) {
        this.id = id;
        this.nome = nome;
        this.listaDispositivos = listaDispositivos;
    }


   
    
}

