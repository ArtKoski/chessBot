package datastructureproject.Board;

/**
 *
 * @author artkoski
 */
public enum Side {
    WHITE,
    BLACK;

    public Side flip() {
        return this.equals(Side.WHITE) ? Side.BLACK : Side.WHITE;
    }

    public String value() {
        return name();
    }

    public static Side fromValue(String v) {
        return valueOf(v);
    }
}
