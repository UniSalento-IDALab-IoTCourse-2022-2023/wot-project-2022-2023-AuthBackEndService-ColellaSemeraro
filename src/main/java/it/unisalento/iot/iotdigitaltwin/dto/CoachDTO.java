package it.unisalento.iot.iotdigitaltwin.dto;

public class CoachDTO extends UserDTO{

    String squadra;

    String ruoloAllenato;

    public String getSquadra() {
        return squadra;
    }

    public void setSquadra(String squadra) {
        this.squadra = squadra;
    }

    public String getRuoloAllenato() {
        return ruoloAllenato;
    }

    public void setRuoloAllenato(String ruoloAllenato) {
        this.ruoloAllenato = ruoloAllenato;
    }

}
