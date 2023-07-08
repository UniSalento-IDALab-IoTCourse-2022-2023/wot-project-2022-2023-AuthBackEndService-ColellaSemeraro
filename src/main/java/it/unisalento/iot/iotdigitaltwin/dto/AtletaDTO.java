package it.unisalento.iot.iotdigitaltwin.dto;

public class AtletaDTO extends UserDTO{

    int altezza;

    double peso;

    String squadra;

    String posizioneCampo;

    String idCoach;

    public String getIdCoach() {
        return idCoach;
    }

    public void setIdCoach(String idCoach) {
        this.idCoach = idCoach;
    }

    public int getAltezza() {
        return altezza;
    }

    public void setAltezza(int altezza) {
        this.altezza = altezza;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getSquadra() {
        return squadra;
    }

    public void setSquadra(String squadra) {
        this.squadra = squadra;
    }

    public String getPosizioneCampo() {
        return posizioneCampo;
    }

    public void setPosizioneCampo(String posizioneCampo) {
        this.posizioneCampo = posizioneCampo;
    }
}
