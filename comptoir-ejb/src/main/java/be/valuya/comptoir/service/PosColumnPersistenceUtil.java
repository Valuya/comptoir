package be.valuya.comptoir.service;

import be.valuya.comptoir.model.commercial.Pos;
import be.valuya.comptoir.model.commercial.Pos_;
import be.valuya.comptoir.util.pagination.PosColumn;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
public class PosColumnPersistenceUtil {

    public static Path<?> getPath(From<?, Pos> posFrom, PosColumn posColumn) {
        switch (posColumn) {
            case NAME:
                return posFrom.get(Pos_.name);
            case DESCRIPTION:
                return posFrom.get(Pos_.description);
            default:
                throw new AssertionError(posColumn.name());
        }
    }

}
