package dao;

import generated.tables.records.ReceiptsRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.impl.DSL;

import java.util.ArrayList;
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
        return dsl.select().from(RECEIPTS_TAGS)
                .join(RECEIPTS)
                .on(RECEIPTS.ID.equal(RECEIPTS_TAGS.ID))
                .where(RECEIPTS_TAGS.TAG.equal(tagName))
                .fetchInto(RECEIPTS);
    }

    public List<String> getTagsByReceipts(int receiptID){
        Result<Record1<String>> listTags = dsl.select(RECEIPTS_TAGS.TAG).from(RECEIPTS_TAGS)
                           .where(RECEIPTS_TAGS.ID.equal(receiptID))
                           .fetch();

        List<String> listTagsString = new ArrayList<String>();
        for (Record1<String> tag: listTags){
            listTagsString.add(tag.value1());
        }
        return listTagsString;
    }


}
