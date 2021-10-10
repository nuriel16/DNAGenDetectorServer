/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template DNAFile, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import gendetecor.GenDetector;
import static gendetecor.GenDetector.GEN_PREFIX;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 *
 * @author Sara Novick
 */
@Path("")
public class GenEntryPoint {

    @GET
    public String test() {
        return "OK";
    }

    @GET
    @Path("find/{gen: .*}")
    public Response isGenExists(@PathParam("gen") String gen) {
        if (gen == null || !gen.startsWith(GEN_PREFIX)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {
            boolean exists = GenDetector.getInstance().genExists(gen.substring(GEN_PREFIX.length()));
            return exists
                    ? Response.ok("Gen exists on file.").build()
                    : Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }
}
