package it.uninsubria.studenti.rripamonti.biblioteca.rest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by rober on 08/07/2017.
 */

public interface BookCoverRestService {

    final public static String AWSAccessKeyId = "";
    final public static String AssociateTag = "";
    @GET("/onca/xml?Service=AWSECommerceService&Operation=ItemLookup&ResponseGroup=Images&IdType=ASIN&" +
            "&AWSAccessKeyId=" + AWSAccessKeyId +"&AssociateTag=" + AssociateTag)
    Call<BookCoverContainer> getBookCoverInfo(@Query("ItemID") String asin);

}
