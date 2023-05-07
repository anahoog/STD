package engtelecom.std;

public class Dispositivos {
  private String tipo;
  private String estado = "desligado";


    public Dispositivos(String tipo, String estado) {
      this.tipo = tipo;
      this.estado = estado;
    }
  
    public String getTipo() {
      return tipo;
    }

    public void setTipo(String tipo) {
      this.tipo = tipo;
    }

    public void setEstado(String estado) {
      this.estado = estado;
    }

    public String getEstado() {
      return this.estado;
    }
  
    public void ligar() {
        this.estado = "ligado";
    }
    public void desligar() {
        this.estado = "desligado";
    }
  
  
  }