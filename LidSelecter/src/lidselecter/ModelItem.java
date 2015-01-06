package lidselecter;

/**
 *
 * @author chris
 */
public class ModelItem {

    public int id;
    public String voornaam;
    public String achternaam;
    public String datum;
    public String inschrijfKosten;
    public String maxInschrijf;
    public String maxPTafel;
    public String kaartCode;
    public String locatieCode;
    public String kaartType;

    @Override
    public String toString() {
        String description = voornaam+" "+achternaam;
        return description;
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
    public String locatieCode() {
        return locatieCode;
    }
    public String kaartType() {
        return kaartType;
    }
}
