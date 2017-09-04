package controllers;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import api.ReceiptResponse;
import dao.TagDao;
import generated.tables.records.ReceiptsRecord;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Path("/tags")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TagController {

    final TagDao tags;

    public TagController(TagDao tags) {
        this.tags = tags;
    }

    @PUT
    @Path("/{tag}")
    public int toggleTag(@PathParam("tag") String tagName, int receiptID) {

        if (tags.tagExists(tagName, receiptID)){
            tags.deleteTag(tagName, receiptID);
            return 1;
        } else {
            tags.insertTag(tagName, receiptID);
            return 0;
        }

    }

    @GET
    @Path("/{tag}")
    public List<ReceiptResponse> getReceipts(@PathParam("tag") String tagName){
        List<ReceiptsRecord> receiptRecords = tags.getReceiptsByTags(tagName);
        return receiptRecords.stream().map(ReceiptResponse::new).collect(toList());
    }
}
