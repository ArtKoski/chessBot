package datastructureproject.Board;

/**
 *
 * @author artkoski
 */
public class Move extends Object {

    Square from;
    Square to;
    Piece promotion = Piece.NONE;

    public Move(Square from, Square to) {
        this.from = from;
        this.to = to;
    }

    public Move(Square from, Square to, Piece promotion) {
        this(from, to);
        this.promotion = promotion;
    }

    public Square getFrom() {
        return this.from;
    }

    public Square getTo() {
        return this.to;
    }

    public Piece getPromotion() {
        return promotion;
    }

    /**
     * Copy paste from chesslib
     *
     * @return
     */
    @Override
    public String toString() {
        String promo = "";
        if (!Piece.NONE.equals(promotion)) {
            promo = Notation.getPieceNotation(promotion);
        }
        return from.toString().toLowerCase()
                + to.toString().toLowerCase()
                + promo.toLowerCase();
    }
}
