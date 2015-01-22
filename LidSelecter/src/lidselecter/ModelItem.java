package lidselecter;

/**
 *
 * @author chris
 */
public class ModelItem {

    public int id;
    public int isBetaald;
    public int aantalFiches;
    public String idTafel;
    public String positie;
    public int eindPositie = 0;
    public String voornaam;
    public String achternaam;
    public String datum;
    public String inschrijfKosten;
    public String maxInschrijf;
    public String maxPTafel;
    public String kaartCode;
    public int locatieCode;
    public String kaartType;
    public String naam;
    public int rating;

    @Override
    public String toString() {
        String description;
        if(null==naam&&eindPositie!=0)
        {
            description = " "+eindPositie+"  "+voornaam+" "+achternaam; 
        } else if(null==naam)
        {
            description = voornaam+" "+achternaam;
        }else
        {
            description = naam;   
        }     
        return description;
    }
    
    public String toNaam(){
        return naam;
    }

    public String toDatum() {
        return datum;
    }

    public String toInschrijfKosten() {
        return inschrijfKosten;
    }
    public String maxInschrijf() {
        return maxInschrijf;
    }
    public String maxPTafel() {
        return maxPTafel;
    }
    public String kaartCode() {
        return kaartCode;
    }
    public int locatieCode() {
        return locatieCode;
    }
    public String kaartType() {
        return kaartType;
    }
}
