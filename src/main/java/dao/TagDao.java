package dao;

import api.ReceiptResponse;
import generated.tables.records.ReceiptsRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static generated.Tables.RECEIPTS;
import static generated.Tables.RECEIPTS_TAGS;

public class TagDao {

    DSLContext dsl;

    public TagDao(Configuration jooqConfig) {
        this.dsl = DSL.using(jooqConfig);
    }


    public boolean tagExists(String tagName, int receiptID){
        int numRows  = dsl.selectCount()
                .from(RECEIPTS_TAGS)
                .where(RECEIPTS_TAGS.ID.equal(receiptID))
                .and(RECEIPTS_TAGS.TAG.equal(tagName))
                .fetchOne(0, Integer.class);
        return numRows > 0;
    }

    public void insertTag(String tagName, int receiptID){
        dsl.insertInto(RECEIPTS_TAGS, RECEIPTS_TAGS.ID, RECEIPTS_TAGS.TAG)
                .values(receiptID, tagName)
                .execute();

    }

    public void deleteTag(String tagName, int receiptID){
        dsl.delete(RECEIPTS_TAGS)
                .where(RECEIPTS_TAGS.ID.equal(receiptID)
                        .and(RECEIPTS_TAGS.TAG.equal(tagName)))
                .execute();

    }

    public List<ReceiptsRecord> getReceiptsByTags(String tagName){
        // select from receipts_tags
        // join two tables ON tag IDs, receipt IDs
        // where tags =
        // fetch into
        return dsl.select().from(RECEIPTS_TAGS)
                .join(RECEIPTS)
                .on(RECEIPTS.ID.equal(RECEIPTS_TAGS.ID))
                .where(RECEIPTS_TAGS.TAG.equal(tagName))
                .fetchInto(RECEIPTS);
    }


}
