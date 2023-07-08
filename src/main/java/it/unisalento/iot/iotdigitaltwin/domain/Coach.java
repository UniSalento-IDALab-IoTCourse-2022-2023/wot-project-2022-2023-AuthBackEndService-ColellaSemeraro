package it.unisalento.iot.iotdigitaltwin.domain;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("coach")
public class Coach extends User{

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
