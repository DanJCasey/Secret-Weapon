package rocks.zipcode.domain.enumeration;

/**
 * The Expertise enumeration.
 */
public enum Expertise {
    PLUMBING("Plumbing"),
    ELECTRICIAN("Electrician"),
    CARPENTER("Carpenter"),
    MECHANIC("Mechanic");

    private final String value;

    Expertise(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
